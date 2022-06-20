from search import astar_search
import performance
import pandas as pd

if __name__ == '__main__':
    print("ASTAR:")
    df = performance.performance_tester_full(astar_search)
    with pd.ExcelWriter('astar.xlsx') as writer:  
        df.to_excel(writer, sheet_name='ASTAR')
    print("DONE!")