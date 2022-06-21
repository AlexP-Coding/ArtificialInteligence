# takuzu.py: Template para implementação do projeto de Inteligência Artificial 2021/2022.
# Devem alterar as classes e funções neste ficheiro de acordo com as instruções do enunciado.
# Além das funções e classes já definidas, podem acrescentar outras que considerem pertinentes.

# Grupo 00:
# 00000 Nome1
# 00000 Nome2

import sys
from sys import stdin

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
        self.maxNrsPerLine = round(self.size/2) 
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
        if row > 0:
            left = self.rows[row][col-1]
        if col < self.size-1:
            right = self.rows[row][col+1]
        return (left, right)

    @staticmethod
    def parse_instance_from_stdin():
        """Lê o test do standard input (stdin) que é passado como argumento
        e retorna uma instância da classe Board.

        Por exemplo:
            $ python3 takuzu.py < testes_takuzu/input_T01

            > from sys import stdin
            > stdin.readline()
        """
        size = int(stdin.readline())
        board = Board(size)
        for row in range(size):
            list_row = stdin.readline().rstrip('\n').split('\t')
            list_row = [int(x) for x  in list_row]
            board.rows[row] = list_row
            for col in range (size):
                board.cols[col].append(list_row[col])
        
        for row in range(board.size):
            for col in range(board.size):
                if board.rows[row][col] == 0:
                    board.rowStatus["Zeroes"][row] += 1
                    board.colStatus["Zeroes"][col] += 1
                elif board.rows[row][col] == 1:
                    board.rowStatus["Ones"][row] += 1
                    board.colStatus["Ones"][col] += 1
                else:
                    board.rowStatus["Missing"][row] += 1
                    board.colStatus["Missing"][col] += 1
                    board.nrMissing += 1
                    board.nrFound -= 1

        return board

    def to_string(self):
        stringRows = ""
        for i in range(self.size):
            for j in range(self.size):
                stringRows += str(self.rows[i][j])
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
        for row in self.rows:
            if self.rows.count(row) > 1:
                return True
        for col in self.cols:
            if self.cols.count(col) > 1:
                return True
        return False

    def has_3_in_a_line(self):
        for col in range(self.size):
            for row in range(self.size):
                adj_h = self.adjacent_horizontal_numbers(row, col)
                if self.rows[row][col] == adj_h[0] == adj_h[1]:
                    return True
                adj_v = self.adjacent_vertical_numbers(col, row)
                if self.cols[row][col] == adj_v[0] == adj_v[1]:
                    return True
        return False

    def act(self, action):
        """Aplica acao"""

    # TODO: outros metodos da classe


class Takuzu(Problem):
    def __init__(self, board: Board):
        self.board = board
        self.maxNrsPerLine = round(self.board.size/2) 
        self.nrTotal = self.board.size**2
        self.nrFound = self.nrTotal
        self.nrMissing = 0
        self.rowStatus = {
            "Zeroes": [0] * self.board.size,
            "Ones": [0] * self.board.size,
            "Missing": [0] * self.board.size
        }
        self.colStatus = {
            "Zeroes": [0] * self.board.size,
            "Ones": [0] * self.board.size,
            "Missing": [0] * self.board.size
        }

        for row in range(self.board.size):
            for col in range(self.board.size):
                if self.board.rows[row][col] == 0:
                    self.rowStatus["Zeroes"][row] += 1
                    self.colStatus["Zeroes"][col] += 1
                elif self.board.rows[row][col] == 1:
                    self.rowStatus["Ones"][row] += 1
                    self.colStatus["Ones"][col] += 1
                else:
                    self.rowStatus["Missing"][row] += 1
                    self.colStatus["Missing"][col] += 1
                    self.nrMissing += 1
                    self.nrFound -= 1

    def to_string(self):
        string = ""
        string += self.board.to_string_status()
        return string

    def actions(self, state: TakuzuState):
        """Retorna uma rows de ações que podem ser executadas a
        partir do estado passado como argumento."""
        """available = []

       for row in range(self.board.size):
            if num
            for col in range(self.board.si)
        """

        # TODO
        pass

    def result(self, state: TakuzuState, action):
        """Retorna o estado resultante de executar a 'action' sobre
        'state' passado como argumento. A ação a executar deve ser uma
        das presentes na rows obtida pela execução de
        self.actions(state)."""
        val = action[2]
        state.board.rows[action[0]][action[1]] = val
        state.board.cols[action[1]][action[0]] = val
        state.nrFound +=1
        state.nrMissing -= 1
        if val == 0:
            state.rowStatus["Zeroes"][action[0]] += 1
            state.colStatus["Zeroes"][action[1]] += 1
        elif val == 1:
            state.rowStatus["Ones"][action[0]] += 1
            state.colStatus["Ones"][action[1]] += 1
        state.rowStatus["Missing"][action[1]] -= 1
        state.colStatus["Missing"][action[1]] -= 1
        return state

    def goal_test(self, state: TakuzuState):
        """Retorna True se e só se o estado passado como argumento é
        um estado objetivo. Deve verificar se todas as posições do tabuleiro
        estão preenchidas com uma sequência de números adjacentes."""
        if self.nrMissing != 0:
            return False  
        elif self.board.has_duplicates() or self.board.has_3_in_a_line():
            return False
        else:
            return True
            
        # TODO

    def h(self, node: Node):
        """Função heuristica utilizada para a procura A*."""
        # TODO
        pass

    # TODO: outros metodos da classe


if __name__ == "__main__":
    board = Board.parse_instance_from_stdin()
    print(board.to_string())
    takuzu = Takuzu(board)
    print(takuzu.to_string())

    # TODO:
    # Ler o ficheiro do standard input,
    # Usar uma técnica de procura para resolver a instância,
    # Retirar a solução a partir do nó resultante,
    # Imprimir para o standard output no formato indicado.
    