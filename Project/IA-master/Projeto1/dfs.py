from search import depth_first_tree_search
import performance
import pandas as pd

if __name__ == '__main__':
    print("DFS:")
    df = performance.performance_tester_full(depth_first_tree_search)
    with pd.ExcelWriter('dfs.xlsx') as writer:  
        df.to_excel(writer, sheet_name='DFS')
    print("DONE!")