import pandas as pd
import datetime
from performance_ricochet_robots import parse_instance, RicochetRobots
from search import astar_search, breadth_first_tree_search, depth_first_tree_search, greedy_search
import threading

def execute_search_algorithm(search_algorithm, problem, result):
    result[0] = search_algorithm(problem)

def performance_tester(filename, search_algorithm, test_number):
    
    # Traducao do problema para tabuleiro
    t1 = datetime.datetime.now()
    initial_board = parse_instance(filename)
    problem = RicochetRobots(initial_board)
    t2 = datetime.datetime.now()

    # Obter o no solucao usando a procura astar:
    t3 = datetime.datetime.now()
    depth = states = expanded = tested = None
    result = [None]
    p = threading.Thread(target=execute_search_algorithm, name="AI Search", args=(search_algorithm, problem, result,))
    p.daemon = True
    p.start()
    # execute_search_algorithm(search_algorithm, problem)
    p.join(60)
    if not result[0]:
        delta1 = (t2 - t1).total_seconds()
        delta2 = depth = states = expanded = tested = "TIMEOUT"
        print("Terminated test {}: Timeout".format(test_number))
    else:
        t4 = datetime.datetime.now()
        delta1 = (t2 - t1).total_seconds()
        delta2 = (t4 - t3).total_seconds()
        depth = result[0].depth
        states = problem.states
        expanded = problem.expanded
        tested = problem.goal_tests
        print("Terminated test {}: Success".format(test_number))


    # Devolve estatisticas da procura (Numero de Jogadas, Tempo de traducao, Tempo de execucao, Nos Criados, Nos Expandidos, Nos Testados)
    return [depth, delta1, delta2, states, expanded, tested]

def performance_tester_full(search_algorithm):
    data = []
    index = []
    for i in range(12):
        for j in range(1):
            ret = performance_tester("performance_tests/{}x{}/{}.rr".format(i+5,i+5,j+1),search_algorithm, "{}x{}".format(i+5,i+5))
            data.append(["{}.rr".format(j+1)]+ret)
            index.append("{}x{}".format(i+5, i+5))
    col = ["Filename", "Depth", "Tempo de Traducao", "Tempo de execucao", "Estados Gerados", "Estados Expandidos", "Estados Testados"]
    return pd.DataFrame(data, index=index, columns=col)

