from search import greedy_search
import performance
import pandas as pd

if __name__ == '__main__':
    print("GREEDY:")
    df = performance.performance_tester_full(greedy_search)
    with pd.ExcelWriter('greedy.xlsx') as writer:  
        df.to_excel(writer, sheet_name='greedy')
    print("DONE!")