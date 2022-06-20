# Grupo 47
# 92485 Joana Rocha Raposo
# 92569 Vicente Aser Marques Rebelo Castillo Lorenzo

# ------------------------------------------ Imports ------------------------------------------
from search import Problem, Node, astar_search, breadth_first_tree_search, depth_first_tree_search, greedy_search
from copy import deepcopy
import sys

# ------------------------------------------ Global Variable ------------------------------------------
static_board = None # Variavel global que guarda o tabuleiro estatico


# ------------------------------------------ Classes ------------------------------------------
"""
Representa cada quadricula do tabuleiro estatico.
Atributos da classe:
    - x e y (Indicam as coordenadas da quadricula no tabuleiro);
    - up, down, right e left (Apontadores para as quadriculas adjacentes. Se tiverem None e' uma barreira);
    - color (Se essa quadricula for o target guarda o target. Caso contrario, guarda None);
"""
class StaticBoardNode:

    def __init__(self, x: int, y: int):
        self.x = x
        self.y = y
        self.up = None
        self.down = None
        self.right = None
        self.left = None
        self.color = None  # Se for target guarda a cor do mesmo


"""
Representa o tabuleiro estatico = quadriculas + barreiras.
Atributos da classe:
    - size (Guarda o tamanho do tabuleiro);
    - board (Guarda o tabuleiro estatico);
    - target (Guarda o StaticBoardNode que representa o target);
Metodos da classe:
    - add_barrier() (Adiciona uma barreira ao tabuleiro apagando os apontadores entre quadriculas);
    - add_target() (Adiciona um target ao tabuleiro = Guardar a cor no StaticBoardNode e registar o target);
    - get_target_info() (Devolve toda a informacao do target = Cor + Coordenadas);
    - get_node() (Recebe coordenadas normais e devolve o StaticBoardNode da matrix com essas coordenadas);
Objetivo da classe: Evitar que em cada estado se tenha que criar uma copia do tabuleiro estatico, evitando
problemas de Memory Exceeded.
"""
class StaticBoard():

    def __init__(self, size: int):
        # Cria uma matrix (size * size) com as respestivas quadriculas (StaticBoardNode).
        # Desta maneira cria-se um tabuleiro com barreiras em todas as arestas porque todas
        # as quadriculas tem up, down, left e right igual a None.
        self.board = [[StaticBoardNode(i, j) for j in range(size)] for i in range(size)]
        
        # Adiciona as respetivas ligacoes entre quadriculas, criando uma tabuleiro sem
        # barreiras excepto nas margens do mesmo.
        for i in range(size):
            for j in range(size):
                try:
                    self.board[i][j].down = self.board[i + 1][j]
                except IndexError:
                    pass
                try:
                    self.board[i + 1][j].up = self.board[i][j]
                except IndexError:
                    pass
                try:
                    self.board[i][j].right = self.board[i][j + 1]
                except IndexError:
                    pass
                try:
                    self.board[i][j + 1].left = self.board[i][j]
                except IndexError:
                    pass
        
        # Na inicializacao ainda nao ha nenhum StaticBoardNode que seja target.
        self.target = None

    
    def add_barrier(self, x: int, y: int, side: str):
        this_node = self.get_node(x, y)
        if side == "r" and this_node.right:
            neighbour_node = self.get_node(x, y + 1)
            this_node.right = neighbour_node.left = None
        elif side == "l" and self.board[x - 1][y - 1].left:
            neighbour_node = self.get_node(x, y - 1)
            this_node.left = neighbour_node.right = None
        elif side == "u" and self.board[x - 1][y - 1].up:
            neighbour_node = self.get_node(x - 1, y)
            this_node.up = neighbour_node.down = None
        elif side == "d" and self.board[x - 1][y - 1].down:
            neighbour_node = self.get_node(x + 1, y)
            this_node.down = neighbour_node.up = None
        else:
            raise NotImplementedError("add_barrier")

    def add_target(self, color: str, x: int, y: int):
        self.target = self.get_node(x, y)
        self.target.color = color

    def get_target_info(self):
        return self.target.color, self.target.x+1, self.target.y+1

    def get_node(self, x: int, y: int):
        return self.board[x - 1][y - 1]


"""
Representa o tabuleiro dinamico = robot vermelho, amarelo, azul e verde.
Atributos da classe:
    - robots (Dicionario que guarda um tuplo com as coordenadas do robot);
    - size (Tamanho do tabuleiro);
Metodos da classe:
    - get_robot() (Recebe uma cor e devolve o tuplo com as coordenadas do robot com essa cor)
    - set_robot() (Recebe uma cor e regista um novo tuplo com as coordenadas do robot)
    - get_robot_coord() (Recebe uma cor e devolve as coordenadas do robot com essa cor)
    - set_robot_coord() (Recebe uma cor e regista novas coordenadas do robot com essa cor)
    - go_left() (Recebe uma cor e devolve as coordenadas do robot caso este va para a esquerda. Caso nao seja possivel devolve None)
    - go_right() (Recebe uma cor e devolve as coordenadas do robot caso este va para a direita. Caso nao seja possivel devolve None)
    - go_up() (Recebe uma cor e devolve as coordenadas do robot caso este va para cima. Caso nao seja possivel devolve None)
    - go_down() (Recebe uma cor e devolve as coordenadas do robot caso este va para baixo. Caso nao seja possivel devolve None)
    - possible_movements() (Devolve uma lista com todas as possiveis movimentacoes de robots)
    - perform_movements() (Recebe uma cor e uma movimentacao e executa essa movimentacao no robot, caso esta seja possivel)
    - check_objctive() (Verifica se o robot com a cor do target se encontra na quadricula do target)
    - robot_position() (Recebe uma cor e devolve as coordenadas do respetivo robot no formato string pretendido para o output)
"""
class Board:

    def __init__(self, size: int):
        self.robots = {"R": None, "G": None, "B": None, "Y": None}
        self.size = size

    def get_robot(self, color):
        return self.robots[color]

    def set_robot(self, color: str, val):
        self.robots[color] = val

    def get_robot_coord(self, color: str):
        ret = self.get_robot(color)
        return ret[0], ret[1]

    def set_robot_coord(self, color: str, x: int, y: int):
        self.set_robot(color, (x, y))

    def go_left(self, color: str):
        # Verifica se ocorrer colisao e guarda as suas coordenadas
        coll_y = -1  # Variavel que guarda a coordenada y onde pode ocorrer colisao de robots
        init_x, init_y = self.get_robot_coord(color)
        for aux_color in self.robots.keys():
            if (aux_color == color):
                continue # Se for o robot pretendido salta a iteracao
            aux_x, aux_y = self.get_robot_coord(aux_color)
            if aux_x == init_x and aux_y < init_y:
                coll_y = max(coll_y, aux_y) # Apenas regista em que robot colide primeiro

        # Verifica se antes de colidir com um robot colide com uma barreira
        ret = static_board.get_node(init_x, init_y)
        aux_y = init_y
        while ret.left and aux_y - 1 != coll_y:
            aux_y = aux_y - 1
            ret = static_board.get_node(init_x, aux_y)
        if init_y == aux_y: #se robot nao poder mover se
            return None
        
        # Devolve as novas coordenadas do robot caso se faca esta movimentacao
        return (init_x, aux_y)

    def go_right(self, color: str):
        # Verifica se ocorrer colisao e guarda as suas coordenadas
        coll_y = self.size + 1  # Variavel que guarda a coordenada y onde pode ocorrer colisao de robots
        init_x, init_y = self.get_robot_coord(color)
        for aux_color in self.robots.keys():
            if (aux_color == color):
                continue # Se for o robot pretendido salta a iteracao
            aux_x, aux_y = self.get_robot_coord(aux_color)
            if aux_x == init_x and aux_y > init_y:
                coll_y = min(coll_y, aux_y) # Apenas regista em que robot colide primeiro

        # Verifica se antes de colidir com um robot colide com uma barreira
        ret = static_board.get_node(init_x, init_y)
        aux_y = init_y
        while ret.right and aux_y + 1 != coll_y:
            aux_y = aux_y + 1
            ret = static_board.get_node(init_x, aux_y)
        if init_y == aux_y: #se robot nao poder mover se
            return None
        
        # Devolve as novas coordenadas do robot caso se faca esta movimentacao
        return (init_x, aux_y)

    def go_down(self, color: str):
        # Verifica se ocorrer colisao e guarda as suas coordenadas
        coll_x = self.size + 1  # Variavel que guarda a coordenada x onde pode ocorrer colisao de robots
        init_x, init_y = self.get_robot_coord(color)
        for aux_color in self.robots.keys():
            if (aux_color == color):
                continue # Se for o robot pretendido salta a iteracao
            aux_x, aux_y = self.get_robot_coord(aux_color)
            if aux_y == init_y and aux_x > init_x:
                coll_x = min(coll_x, aux_x) # Apenas regista em que robot colide primeiro

        # Verifica se antes de colidir com um robot colide com uma barreira
        ret = static_board.get_node(init_x, init_y)
        aux_x = init_x
        while ret.down and aux_x + 1 != coll_x:
            aux_x = aux_x + 1
            ret = static_board.get_node(aux_x, init_y)
        if init_x == aux_x: #se robot nao poder mover se
            return None
        
        # Devolve as novas coordenadas do robot caso se faca esta movimentacao
        return (aux_x, init_y)

    def go_up(self, color: str):
        # Verifica se ocorrer colisao e guarda as suas coordenadas
        coll_x = -1  # Variavel que guarda a coordenada x onde pode ocorrer colisao de robots
        init_x, init_y = self.get_robot_coord(color)
        for aux_color in self.robots.keys():
            if (aux_color == color):
                continue # Se for o robot pretendido salta a iteracao
            aux_x, aux_y = self.get_robot_coord(aux_color)
            if aux_y == init_y and aux_x < init_x:
                coll_x = max(coll_x, aux_x) # Apenas regista em que robot colide primeiro

        # Verifica se antes de colidir com um robot colide com uma barreira
        ret = static_board.get_node(init_x, init_y)
        aux_x = init_x
        while ret.up and aux_x - 1 != coll_x:
            aux_x = aux_x - 1
            ret = static_board.get_node(aux_x, init_y)
        if init_x == aux_x: #se robot nao poder mover se
            return None

        # Devolve as novas coordenadas do robot caso se faca esta movimentacao
        return (aux_x, init_y)

    def possible_movements(self):
        actions = []
        for r_color in self.robots.keys():
            if self.go_up(r_color):
                actions.append((r_color, 'u'))
            if self.go_down(r_color):
                actions.append((r_color, 'd'))
            if self.go_left(r_color):
                actions.append((r_color, 'l'))
            if self.go_right(r_color):
                actions.append((r_color, 'r'))
        return actions

    def perform_movements(self, r_color:str, action:str):
        # Neste metodo, os tries verificam se e' possivel fazer o movimento
        if action == "u":
            try:
                x, y = self.go_up(r_color)
            except TypeError:
                pass
            else:
                self.set_robot_coord(r_color, x, y)
        elif action == "d":
            try:
                x, y = self.go_down(r_color)
            except TypeError:
                pass
            else:
                self.set_robot_coord(r_color, x, y)
        elif action == "l":
            try:
                x, y = self.go_left(r_color)
            except TypeError:
                pass
            else:
                self.set_robot_coord(r_color, x, y)
        elif action == "r":
            try:
                x, y = self.go_right(r_color)
            except TypeError:
                pass
            else:
                self.set_robot_coord(r_color, x, y)
        else:
            raise NotImplementedError("perform_movements")

    def check_objective(self):
        t_color, t_x, t_y = static_board.get_target_info()
        r_x, r_y = self.get_robot_coord(t_color)
        return t_x == r_x and t_y == r_y

    def robot_position(self, color: str):
        return self.get_robot(color)


"""
Representa um estado do tabuleiro em dada jogada.
"""
class RRState:
    state_id = 0

    def __init__(self, board):
        self.board = board
        self.id = RRState.state_id
        RRState.state_id += 1

    def __lt__(self, other):
        return self.id < other.id


"""
Representa o problema de procura com algoritmos de IA da solucao.
"""
class RicochetRobots(Problem):

    def __init__(self, board: Board):
        super().__init__(RRState(board))

    def actions(self, state: RRState):
       return state.board.possible_movements()

    def result(self, state: RRState, action):
        ret: RRState = deepcopy(state) 
        ret.board.perform_movements(action[0], action[1])
        return ret

    def goal_test(self, state: RRState):
        return state.board.check_objective() 

    def h(self, node: Node):
        """ Funcao heuristica utilizada para a procura A*. """
        t_color, t_x, t_y = static_board.get_target_info() #cor e posicao do target
        rc_x, rc_y = node.state.board.get_robot_coord(t_color) #posicao do robot da cor do target
        distance_rc = abs(rc_x - t_x) + abs(rc_y - t_y) #distancia de Manhattan do robot da cor do target ao target
        
        #variaveis que irao representar as distancias dos outros 3 robots:
        #o que esta mais longe do target, o que esta mais perto, e o que intermedio
        max_dist = 0
        min_dist = node.state.board.size
        med_dist = 0
        for colour in ['R','B','G','Y']: #ciclo para atribuir as distancias respetivas acima definidas
            if colour != t_color:
                x,y= node.state.board.get_robot_coord(colour)
                distance = abs(x - t_x) + abs(y - t_y) #distancia de Mahattan
                if distance > max_dist:
                    max_dist = distance
                elif distance < min_dist:
                    min_dist = distance
                else:
                    med_dist = distance
                
        #heuristica: combinacao linear das 4 distancias calculadas, atribuindo um peso maior as distancias maiores
        #ou seja, quanto mais longe os robots estiverem do target, maior o valor da heuristica
        return distance_rc + 4*max_dist + 3*med_dist + 2*min_dist 
   
# ------------------------------------------ Functions ------------------------------------------
def parse_instance(filename: str) -> Board:
    global static_board
    with open(filename, 'r') as f:
        size = int(f.readline()) # Le o tamanho do tabuleiro (M)
        static_board = StaticBoard(size) # Cria o tabuleiro estatico e guarda-o na variavel global
        board = Board(size) # Cria o tabuleiro dinamico inicial
        
        # Le e adiciona o robots ao tabuleiro dinamico inicial
        for _ in range(4):
            line = f.readline().split()
            board.set_robot_coord(line[0], int(line[1]), int(line[2]))
        line = f.readline().split()
        
        # Le e adiciona o target ao tabuleiro estatico
        static_board.add_target(line[0], int(line[1]), int(line[2]))
        for _ in range(int(f.readline())):
            line = f.readline().split()
            static_board.add_barrier(int(line[0]), int(line[1]), line[2])
    
    # Retorna o tabuleiro dinamico
    return board


# ------------------------------------------ Main ------------------------------------------
if __name__ == "__main__":
    # Ler o ficheiro e criar a Board inicial
    initial_board = parse_instance(sys.argv[1])

    # Criar uma instancia de RicochetRobots:
    problem = RicochetRobots(initial_board)

    # Obter o no solucao usando a procura astar:
    solution_node = astar_search(problem)

    # Imprimir a solucao
    print(solution_node.depth) # Imprime o M
    for move in solution_node.solution():
        print("{0} {1}".format(move[0], move[1]))