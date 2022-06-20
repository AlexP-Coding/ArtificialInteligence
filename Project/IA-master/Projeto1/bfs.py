from search import breadth_first_tree_search
import performance
import pandas as pd

if __name__ == '__main__':
    print("BFS:")
    df = performance.performance_tester_full(breadth_first_tree_search)
    with pd.ExcelWriter('bfs.xlsx') as writer:  
        df.to_excel(writer, sheet_name='BFS')
    print("DONE!")