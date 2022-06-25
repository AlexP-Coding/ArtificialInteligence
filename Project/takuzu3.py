# takuzu.py: Template para implementação do projeto de Inteligência Artificial 2021/2022.
# Devem alterar as classes e funções neste ficheiro de acordo com as instruções do enunciado.
# Além das funções e classes já definidas, podem acrescentar outras que considerem pertinentes.

# Grupo 27:
# 7Jyoay
# 91110 Inara Parbato 
# 97375 Alexandra Pato

# python3 takuzu3.py < testes-takuzu/input_T01

from copy import deepcopy
from hashlib import new
import sys
from sys import stdin
from wsgiref.validate import validator

from search import (
    Problem,
    Node,
    astar_search,
    breadth_first_tree_search,
    depth_first_tree_search,
    greedy_search,
    recursive_best_first_search,
)


class TakuzuState:
    state_id = 0

    def __init__(self, board):
        self.board = board
        self.id = TakuzuState.state_id
        TakuzuState.state_id += 1

    def __lt__(self, other):
        return self.id < other.id

    # TODO: outros metodos da classe


class Board:
    """Representação interna de um tabuleiro de Takuzu."""

    def __init__(self, size: int):
        self.rows = [[]] * size
        self.cols = [[]] * size
        self.size = size
        self.maxNrsPerLine = self.size//2 + self.size%2
        self.nrTotal = self.size**2
        self.nrFound = self.nrTotal
        self.nrMissing = 0
        self.rowStatus = {
            "Zeroes": [0] * self.size,
            "Ones": [0] * self.size,
            "Missing": [0] * self.size
        }
        self.colStatus = {
            "Zeroes": [0] * self.size,
            "Ones": [0] * self.size,
            "Missing": [0] * self.size
        }
        
    def replicate(b):
        board = Board(b.size)
        for i in range(b.size):
            board.rows[i] = b.rows[i].copy()
            board.cols[i] = b.cols[i].copy()

        board.nrTotal = b.nrTotal
        board.nrFound = b.nrFound
        board.nrMissing = b.nrMissing
        board.rowStatus["Zeroes"] = b.rowStatus["Zeroes"].copy()
        board.rowStatus["Ones"] = b.rowStatus["Ones"].copy()
        board.rowStatus["Missing"] = b.rowStatus["Missing"].copy()
        board.colStatus["Zeroes"] = b.colStatus["Zeroes"].copy()
        board.colStatus["Ones"] = b.colStatus["Ones"].copy()
        board.colStatus["Missing"] = b.colStatus["Missing"].copy()
        return board

    def get_number(self, row: int, col: int) -> int:
        """Devolve o valor na respetiva posição do tabuleiro."""
        return self.rows[row][col]

    def adjacent_vertical_numbers(self, row: int, col: int) -> (int, int):
        """Devolve os valores imediatamente abaixo e acima,
        respectivamente."""
        up = None
        down = None
        if row < self.size-1:
            down = self.rows[row+1][col]
        if row > 0:
            up = self.rows[row-1][col]
        return (down, up)

    def adjacent_horizontal_numbers(self, row: int, col: int) -> (int, int):
        """Devolve os valores imediatamente à esquerda e à direita,
        respectivamente."""
        left = None
        right = None
        if col > 0:
            left = self.rows[row][col-1]
        if col < self.size-1:
            right = self.rows[row][col+1]
        return (left, right)

    @staticmethod
    def parse_instance_from_stdin():
        """Lê o test do standard input (stdin) que é passado como argumento
        e retorna uma instância da classe Board.

        Por exemplo:
            $ python3 takuzu.py < testes-takuzu/input_T01

            > from sys import stdin
            > stdin.readline()
        """
        size = int(stdin.readline())
        board = Board(size)
        for row in range(size):
            list_row = stdin.readline().split('\t')
            list_row = [int(x) for x  in list_row]
            board.rows[row] = list_row
            board.cols[row] = list_row.copy()
        
        for row in range(board.size):
            for col in range(board.size):
                val = int(board.get_number(row, col))
                if val == 2:
                    board.rows[row][col] = None
                    board.rowStatus["Missing"][row] += 1
                    board.colStatus["Missing"][col] += 1
                    board.nrMissing += 1
                    board.nrFound -= 1
                elif val == 1:
                    board.rowStatus["Ones"][row] += 1
                    board.colStatus["Ones"][col] += 1
                elif val == 0:
                    board.rowStatus["Zeroes"][row] += 1
                    board.colStatus["Zeroes"][col] += 1
                board.cols[col][row] = board.rows[row][col]
        return board

    def to_string2(self):
        stringRows = ""
        for i in range(self.size):
            stringRows += str(self.rows[i]) + '\n'
        return stringRows

    def to_string(self):
        stringRows = ""
        for i in range(self.size):
            for j in range(self.size):
                nr = self.get_number(i, j)
                if nr == None:
                    stringRows += '2'
                else:
                    stringRows += str(nr)

                if j == self.size-1:
                    stringRows += '\n'
                else:
                    stringRows += '\t'
        return stringRows

    def to_string_aux(self):
        stringRows = "Rows:\n"
        stringCols = "Cols:\n"
        for i in range(self.size):
            for j in range(self.size):
                stringRows += str(self.rows[i][j])
                stringCols += str(self.cols[i][j])
                if j == self.size-1:
                    stringRows += '\n'
                    stringCols += '\n'
                else:
                    stringRows += '\t'
                    stringCols += '\t'
        stringFinal = stringRows + '\n' + stringCols
        return stringFinal

    def to_string_status(self):
        string = ""
        string += 'Max # per line:' + '\n' + str(self.maxNrsPerLine) + '\n'
        string += 'Nr Total:' + '\n' + str(self.nrTotal) + '\n'
        string += 'Nr Found:' + '\n' + str(self.nrFound) + '\n'
        string += 'Nr Missing:' + '\n' + str(self.nrMissing) + '\n'
        string += 'Rows:' + '\n' + str(self.rowStatus) + '\n'
        string += 'Cols:' + '\n' + str(self.colStatus)
        return string

    def has_duplicates(self):
        for index1 in range(self.size):
            for index2 in range(self.size):
                if index1 != index2 and \
                (self.rows[index1] == self.rows[index2] \
                or self.cols[index1] == self.cols[index2]):
           #         print("I'M A DUPLICATE BITCH!!!!!!!!!!!!!!!")
           #         print(self.to_string_aux())
                    return True
        return False

    def has_3_in_a_line(self):
        for row in range(self.size):
            for col in range(self.size):
                current = self.get_number(row, col)
                adj_h = self.adjacent_horizontal_numbers(row, col)
                if current == adj_h[0] == adj_h[1] != None:
                    return True
                adj_v = self.adjacent_vertical_numbers(row, col)
                if current == adj_v[0] == adj_v[1] != None:
                    return True
        return False

    def has_nr_overflow(self):
        for index in range(self.size):
            if self.rowStatus["Zeroes"][index] > self.maxNrsPerLine \
                or self.colStatus["Zeroes"][index] > self.maxNrsPerLine \
                or self.rowStatus["Ones"][index] > self.maxNrsPerLine \
                or self.colStatus["Ones"][index] > self.maxNrsPerLine:
                return True
        return False

    def apply(self, action):
        """Aplica acao"""
        val = action[2]
        self.rows[action[0]][action[1]] = val
        self.cols[action[1]][action[0]] = val
        self.nrFound += 1
        self.nrMissing -= 1
        self.rowStatus["Missing"][action[0]] -= 1
        self.colStatus["Missing"][action[1]] -= 1
        if val == 0:
            self.rowStatus["Zeroes"][action[0]] += 1
            self.colStatus["Zeroes"][action[1]] += 1
        elif val == 1:
            self.rowStatus["Ones"][action[0]] += 1
            self.colStatus["Ones"][action[1]] += 1
        return self

    # TODO: outros metodos da classe


class Takuzu(Problem):
    def __init__(self, board: Board):
        super().__init__(TakuzuState(board))

    def to_string(self):
        string = ""
        string += self.board.to_string_status()
        return string

    def actions(self, state: TakuzuState):
        """Retorna uma rows de ações que podem ser executadas a
        partir do estado passado como argumento."""
        available = []
        filled = {}
        for row in range(state.board.size):
            if state.board.rowStatus["Missing"][row] != 0:
                if state.board.rowStatus["Zeroes"][row] == state.board.maxNrsPerLine:
                    for col in range(state.board.size):
                        if state.board.get_number(row, col) == None:
                            available.append((row, col, 1))
                            filled[(row, col)] = True
                if state.board.rowStatus["Ones"][row] == state.board.maxNrsPerLine:
                    for col in range(state.board.size):
                        if state.board.get_number(row, col) == None:
                            available.append((row, col, 0))
                            filled[(row, col)] = True
                        
        """ Only apply if actions is empty, so as to not transverse whole table unnecessarily """
        for col in range(state.board.size):
            if state.board.colStatus["Missing"][col] != 0:
                if state.board.colStatus["Zeroes"][col] == state.board.maxNrsPerLine:
                    for row in range(state.board.size):
                        if state.board.get_number(row, col) == None:
                            available.append((row, col, 1))
                            filled[(row, col)] = True
                if state.board.colStatus["Ones"][col] == state.board.maxNrsPerLine:
                    for row in range(state.board.size):
                        if state.board.get_number(row, col) == None:
                            available.append((row, col, 0))
                            filled[(row, col)] = True

        """ If actions is still empty"""
        for row in range(state.board.size):
            for col in range(state.board.size):
                middle = state.board.get_number(row, col)
                adj = state.board.adjacent_horizontal_numbers(row, col)
                left = adj[0]
                right = adj[1]
                if col > 0 and middle == right != None and left == None: # fill left
                    if middle == 0:
                        available.append((row, col-1, 1))
                    elif middle == 1:
                        available.append((row, col-1, 0))
                    filled[(row, col)] = True
                elif col < state.board.size-1 and left == middle != None and right == None: # fill right
                    if middle == 0:
                        available.append((row, col+1, 1))
                    elif middle == 1:
                        available.append((row, col+1, 0))
                    filled[(row, col)] = True
                elif left == right != None and middle == None: # fill middle/current
                    if left == 0:
                        available.append((row, col, 1))
                    elif left == 1:
                        available.append((row, col, 0))
                    filled[(row, col)] = True

                adj = state.board.adjacent_vertical_numbers(row, col)
                down = adj[0]
                up = adj[1]
                if row < state.board.size-1 and up == middle != None and down == None: # fill down
                    if middle == 0:
                        available.append((row+1, col, 1))
                    elif middle == 1:
                        available.append((row+1, col, 0))
                    filled[(row, col)] = True
                elif row > 0 and middle == down != None and up == None: # fill up
                    if middle == 0:
                        available.append((row-1, col, 1))
                    elif middle == 1:
                        available.append((row-1, col, 0))
                    filled[(row, col)] = True
                elif up == down != None and middle == None: # fill middle/current
                    if up == 0:
                        available.append((row, col, 1))
                    elif up == 1:
                        available.append((row, col, 0))
                    filled[(row, col)] = True

        true_available = list(set(available))
        if len(true_available) == 0:
            for row in range(state.board.size):
                for col in range(state.board.size):
                    if (row, col) not in filled and state.board.get_number(row, col) == None:
                        true_available.append((row, col, 0))
                        true_available.append((row, col, 1))
                        filled[(row, col)] = True
    #    print(str(state.board.to_string()))
    #    print(board.to_string_aux())
     #   print(str(state.board.to_string_status()))
      #  print("Available: " + str(true_available))
        return true_available

#   0*	1*	0	1
#   1	0	0*	1
#   0	1*	1	0
#   1	0*	1*	0*


    def result(self, state: TakuzuState, action):
        """Retorna o estado resultante de executar a 'action' sobre
        'state' passado como argumento. A ação a executar deve ser uma
        das presentes na rows obtida pela execução de
        self.actions(state)."""
        newBoard = state.board.replicate()
        newBoard.apply(action)
        resState = TakuzuState(newBoard)
        return resState
    
    def goal_test(self, state: TakuzuState):
        """Retorna True se e só se o estado passado como argumento é
        um estado objetivo. Deve verificar se todas as posições do tabuleiro
        estão preenchidas com uma sequência de números adjacentes."""
        if state.board.nrMissing > 0:
            return False  
 #       elif state.board.has_nr_overflow():
 #           return False
 #       elif state.board.has_duplicates():
 #           return False
        elif state.board.has_3_in_a_line():
            return False
        else:
            return True
            
        # TODO

    def h(self, node: Node):
        """Função heuristica utilizada para a procura A*."""
        # TODO
        return

    # TODO: outros metodos da classe


if __name__ == "__main__":
    board = Board.parse_instance_from_stdin()
 #   print(board.to_string())
    takuzu = Takuzu(board)
    goal_node = depth_first_tree_search(takuzu)
    print(goal_node.state.board.to_string())

    # TODO:
    # Ler o ficheiro do standard input,
    # Usar uma técnica de procura para resolver a instância,
    # Retirar a solução a partir do nó resultante,
    # Imprimir para o standard output no formato indicado.
    