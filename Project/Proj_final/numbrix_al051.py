# numbrix.py: Template para implementação do projeto de Inteligência Artificial 2021/2022.
# Devem alterar as classes e funções neste ficheiro de acordo com as instruções do enunciado.
# Além das funções e classes já definidas, podem acrescentar outras que considerem pertinentes.

# Grupo 51:
# 83508 Marcia Marques
# 89467 Jenisha Lalgi

import sys
import time

from search import Problem, Node, astar_search, breadth_first_tree_search, depth_first_tree_search, greedy_search, recursive_best_first_search
from copy import deepcopy


class NumbrixState:
    state_id = 0

    def __init__(self, board):
        self.board = board
        self.id = NumbrixState.state_id
        NumbrixState.state_id += 1

    def __lt__(self, other):
        return self.id < other.id


class Board:
    """ Representação interna de um tabuleiro de Numbrix. """
    # Um board tera como atributos o seu tamanho, uma lista de listas representando as posicoes, de forma que [x][y]
    # representara o numero presente na linha x e coluna y, e um dicionario contendo os numeros presentes no board,
    # associados as respetivas coordenadas
    def __init__(self, size: int):
        self.size = size
        self.lista = list()
        self.taken = dict()

    def get_number(self, row: int, col: int) -> int:
        """ Devolve o valor na respetiva posição do tabuleiro. """
        return self.lista[row][col]

    def adjacent_vertical_numbers(self, row: int, col: int) -> (int, int):
        """ Devolve os valores imediatamente abaixo e acima, 
        respectivamente. """
        below = None
        above = None
        if row < self.size-1:
            below = self.lista[row+1][col]
        if row > 0:
            above = self.lista[row-1][col]
        return below, above
    
    def adjacent_horizontal_numbers(self, row: int, col: int) -> (int, int):
        """ Devolve os valores imediatamente à esquerda e à direita, 
        respectivamente. """
        left = None
        right = None
        if col > 0:
            left = self.lista[row][col-1]
        if col < self.size-1:
            right = self.lista[row][col+1]
        return left, right

    @staticmethod    
    def parse_instance(filename: str):
        """ Lê o ficheiro cujo caminho é passado como argumento e retorna
        uma instância da classe Board. """
        with open(filename, 'r') as f:
            size = int(f.readline())
            board = Board(size)
            board.taken = {}
            for row in range(size):
                list_row = f.readline().rstrip('\n').split("\t")
                list_row = [int(x) for x in list_row]
                for col in range(size):
                    n = list_row[col]
                    if n != 0:
                        board.taken[n] = (row, col)
                board.lista.append(list_row)
        return board

    def to_string(self):
        """ Formata o board numa string, por forma a imprimi-lo """
        string = str()
        for i in range(0, self.size):
            for j in range(0, self.size):
                string += str(self.lista[i][j])
                if j == self.size-1:
                    string += '\n'
                else:
                    string += '\t'
        return string
    
    def apply(self, action):
        """ Aplica a acao no board """
        self.lista[action[0]][action[1]] = action[2]
        self.taken[action[2]] = (action[0], action[1])
        return self

    def possible_thru_relations(self, row: int, col: int, n: int):
        """ Retorna True se, colocando o numero n na linha row coluna col,
        este pode ter no minimo 2 relacoes """
        relations = 0
        if n == 1 or n == self.size*self.size:
            relations = relations + 1
        if self.adjacent_horizontal_numbers(row, col)[0] == 0 or self.adjacent_horizontal_numbers(row, col)[0] == n-1 or self.adjacent_horizontal_numbers(row, col)[0] == n+1:
            relations = relations + 1
        if self.adjacent_horizontal_numbers(row, col)[1] == 0 or self.adjacent_horizontal_numbers(row, col)[1] == n-1 or self.adjacent_horizontal_numbers(row, col)[1] == n+1:
            relations = relations + 1
        if self.adjacent_vertical_numbers(row, col)[0] == 0 or self.adjacent_vertical_numbers(row, col)[0] == n-1 or self.adjacent_vertical_numbers(row, col)[0] == n+1:
            relations = relations + 1
        if self.adjacent_vertical_numbers(row, col)[1] == 0 or self.adjacent_vertical_numbers(row, col)[1] == n-1 or self.adjacent_vertical_numbers(row, col)[1] == n+1:
            relations = relations + 1
        if relations >= 2:
            return True
        else:
            return False

    def find_closest_numbers(self, n: int) -> (int, int):
        """ Retorna de entre os numeros presentes no board quais
        sao os mais proximos, superior e inferior, de n """
        smaller = 0
        higher = 0
        for i in range(1, n):
            if i in self.taken:
                smaller = i
        for i in range(n+1, self.size*self.size+1):
            if i in self.taken:
                higher = i
                break
        return smaller, higher


class Numbrix(Problem):
    def __init__(self, board: Board):
        """ O construtor especifica o estado inicial. """
        super().__init__(NumbrixState(board))


    def actions(self, state: NumbrixState):
        """ Retorna uma lista de ações que podem ser executadas a
        partir do estado passado como argumento. """
        possible_numbers = list()
        has_moves = dict()
        # Primeiro, para cada numero ja presente no board, vemos se o seguinte/anterior nao se encontram presentes para
        # que possamos considerar adicionar estes numeros
        # Temos que ver se ha zeros a rodear os numeros presentes e estes serao substituidos pelo numero
        # seguinte/anterior se, ao ficarem naquela posicao for possivel ter duas relacoes (numero anterior e seguinte)
        for el in state.board.taken:
            i = state.board.taken[el][0]
            j = state.board.taken[el][1]
            if (el+1 not in state.board.taken) and (el + 1 <= state.board.size * state.board.size):
                has_moves[el + 1] = 0
                if state.board.adjacent_horizontal_numbers(i, j)[0] == 0:  # se o nr da esquerda e 0
                    if state.board.possible_thru_relations(i, j - 1, el + 1):
                        possible_numbers.append((i, j - 1, el + 1))
                if state.board.adjacent_horizontal_numbers(i, j)[1] == 0:  # se o nr da direita e 0
                    if state.board.possible_thru_relations(i, j + 1, el + 1):
                        possible_numbers.append((i, j + 1, el + 1))
                if state.board.adjacent_vertical_numbers(i, j)[1] == 0:  # se o nr de cima e 0
                    if state.board.possible_thru_relations(i - 1, j, el + 1):
                        possible_numbers.append((i - 1, j, el + 1))
                if state.board.adjacent_vertical_numbers(i, j)[0] == 0:  # se o nr de baixo e 0
                    if state.board.possible_thru_relations(i + 1, j, el + 1):
                        possible_numbers.append((i + 1, j, el + 1))
            if (el-1 not in state.board.taken) and el - 1 > 0:
                has_moves[el - 1] = 0
                if state.board.adjacent_horizontal_numbers(i, j)[0] == 0:  # se o nr da esquerda e 0
                    if state.board.possible_thru_relations(i, j-1, el -1):
                        possible_numbers.append((i,j-1,el -1))
                if state.board.adjacent_horizontal_numbers(i, j)[1] == 0:  # se o nr da direita e 0
                    if state.board.possible_thru_relations(i, j + 1, el - 1):
                        possible_numbers.append((i, j + 1, el - 1))
                if state.board.adjacent_vertical_numbers(i, j)[1] == 0:  # se o nr de cima e 0
                    if state.board.possible_thru_relations(i - 1, j, el - 1):
                        possible_numbers.append((i - 1, j, el - 1))
                if state.board.adjacent_vertical_numbers(i, j)[0] == 0:  # se o nr de baixo e 0
                    if state.board.possible_thru_relations(i + 1, j, el - 1):
                        possible_numbers.append((i + 1, j, el - 1))
        possible_numbers = set(possible_numbers)
        list_aux = list()
        moves_dict = dict()
        # Agora temos que verificar se as nossas acoes possiveis respeitam as distancias entre o numero abaixo mais
        # proximo e o numero acima mais proximo, se nao, nao irao mais pertencer ao conjunto de acoes possiveis
        for el in possible_numbers:
            closest = state.board.find_closest_numbers(el[2])
            closest_smaller = closest[0]
            closest_higher = closest[1]
            if closest_smaller != 0 and closest_higher != 0:
                coordenates_smaller = state.board.taken[closest_smaller]
                coordenates_higher = state.board.taken[closest_higher]
                dist1 = abs(coordenates_smaller[0] - el[0]) + abs(coordenates_smaller[1] - el[1])  # Manhattan distance
                dist2 = abs(coordenates_higher[0] - el[0]) + abs(coordenates_higher[1] - el[1])  # Manhattan distance
                if dist1 <= abs(closest_smaller - el[2]) and dist2 <= abs(closest_higher - el[2]):
                    has_moves[el[2]] = has_moves[el[2]] + 1
                    list_aux.append(el)
                    if el[2] in moves_dict:
                        moves_dict[el[2]].append(el)
                    else:
                        moves_dict[el[2]] = [el]
            else:
                has_moves[el[2]] = has_moves[el[2]] + 1
                list_aux.append(el)
                if el[2] in moves_dict:
                    moves_dict[el[2]].append(el)
                else:
                    moves_dict[el[2]] = [el]
        # Se houver algum numero cujo seguinte ou anterior nao esteja nem no board nem no conjunto de acoes possiveis
        # entao chegamos a um estado impossivel, caso em que retornamos a lista vazia por forma a nao seguir mais nenhum
        # estado descendente deste
        if has_moves:
            for el in has_moves:
                if has_moves[el] == 0:
                    return list()
        else:
            return list()
        # Quando chegamos a este ponto ja temos as acoes possiveis filtradas, pelo que retornamos as acoes possiveis
        # para o numero que tiver menos sitios possiveis
        return moves_dict[min(has_moves, key=has_moves.get)]

    def result(self, state: NumbrixState, action):
        """ Retorna o estado resultante de executar a 'action' sobre
        'state' passado como argumento. A ação a executar deve ser uma
        das presentes na lista obtida pela execução de 
        self.actions(state). """
        res: NumbrixState = deepcopy(state)
        res.board.apply(action)
        return res

    def goal_test(self, state: NumbrixState):
        """ Retorna True se e só se o estado passado como argumento é
        um estado objetivo. Deve verificar se todas as posições do tabuleiro 
        estão preenchidas com uma sequência de números adjacentes. """
        for i in range(0, state.board.size):
            for j in range(0, state.board.size):
                to_test = state.board.lista[i][j]
                if to_test == 0:
                    return False
                else:
                    relations = 0
                    left = state.board.adjacent_horizontal_numbers(i, j)[0]
                    right = state.board.adjacent_horizontal_numbers(i, j)[1]
                    below = state.board.adjacent_vertical_numbers(i, j)[0]
                    above = state.board.adjacent_vertical_numbers(i, j)[1]
                    if left is not None:
                        if left+1 == to_test or left-1 == to_test:
                            relations = relations + 1
                    if right is not None:
                        if right+1 == to_test or right-1 == to_test:
                            relations = relations + 1
                    if below is not None:
                        if below+1 == to_test or below-1 == to_test:
                            relations = relations + 1
                    if above is not None:
                        if above+1 == to_test or above-1 == to_test:
                            relations = relations + 1
                    if relations != 2 and (to_test != 1 and to_test != state.board.size*state.board.size):
                        return False
                    if relations != 1 and (to_test == 1 or to_test == state.board.size*state.board.size):
                        return False
        return True

    def h(self, node: Node):
        """ Função heuristica utilizada para a procura A*. """
        """Numero de quadriculas por preencher"""
        maximum = 0
        for i in range(node.state.board.size):
            for j in range(node.state.board.size):
                if node.state.board.lista[i][j] == 0:
                    maximum = maximum+1
        return maximum


if __name__ == "__main__":
    # Ler o ficheiro de input de sys.argv[1],
    board = Board.parse_instance(sys.argv[1])
    # Criar uma instância de Numbrix:
    problem = Numbrix(board)
    # Usar uma técnica de procura para resolver a instância,
    goal_state = depth_first_tree_search(problem)
    # Imprimir para o standard output no formato indicado.
    print(goal_state.state.board.to_string(), end='', sep="")
