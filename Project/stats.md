succs      / goal_tests / states  / found
expandidos / testados   / gerados / estado objetivo achado


n=4, None =7, adivinhas = 
breadth_first_tree_search   <   7/   8/   7/<__m>
depth_first_tree_search     <   7/   8/   7/<__m>
greedy_search               <   7/   8/   7/<__m>
astar_search                <   7/   8/   7/<__m>

n=6, None =7, adivinhas = 
breadth_first_tree_search   <   7/   8/   7/<__m>
depth_first_tree_search     <   7/   8/   7/<__m>
greedy_search               <   7/   8/   7/<__m>
astar_search                <   7/   8/   7/<__m>

n=8, None =42, adivinhas = 
breadth_first_tree_search   <  46/  47/  46/<__m>
depth_first_tree_search     <  42/  43/  43/<__m>
greedy_search               <  46/  47/  46/<__m>
astar_search                <  46/  47/  46/<__m>

n=9, None =32, adivinhas = 
breadth_first_tree_search   <  33/  34/  34/<__m>
depth_first_tree_search     <  34/  35/  34/<__m>
greedy_search               <  33/  34/  34/<__m>
astar_search                <  33/  34/  34/<__m>

n=10, None =55, adivinhas = 
breadth_first_tree_search   <  65/  66/  66/<__m>
depth_first_tree_search     <  62/  63/  63/<__m>
greedy_search               <  65/  66/  66/<__m>
astar_search                <  65/  66/  66/<__m>

n=12, None =79, adivinhas = 
breadth_first_tree_search   < 115/ 116/ 117/<__m>
depth_first_tree_search     <  99/ 100/ 100/<__m>
greedy_search               < 115/ 116/ 117/<__m>
astar_search                < 115/ 116/ 117/<__m>

n=14, None =69, adivinhas = 
breadth_first_tree_search   <  69/  70/  69/<__m>
depth_first_tree_search     <  69/  70/  69/<__m>
greedy_search               <  69/  70/  69/<__m>
astar_search                <  69/  70/  69/<__m>

n=15, None =19, adivinhas = 
breadth_first_tree_search   <  19/  20/  19/<__m>
depth_first_tree_search     <  19/  20/  19/<__m>
greedy_search               <  19/  20/  19/<__m>
astar_search                <  19/  20/  19/<__m>

n=18, None =139, adivinhas = 
breadth_first_tree_search   < 139/ 140/ 139/<__m>
depth_first_tree_search     < 139/ 140/ 139/<__m>
greedy_search               < 139/ 140/ 139/<__m>
astar_search                < 139/ 140/ 139/<__m>

n=20, None =184, adivinhas = 
breadth_first_tree_search   < 184/ 185/ 184/<__m>
depth_first_tree_search     < 184/ 185/ 184/<__m>
greedy_search               < 184/ 185/ 184/<__m>
astar_search                < 184/ 185/ 184/<__m>

n=21, None =180, adivinhas = 
breadth_first_tree_search   < 180/ 181/ 180/<__m>
depth_first_tree_search     < 180/ 181/ 180/<__m>
greedy_search               < 180/ 181/ 180/<__m>
astar_search                < 180/ 181/ 180/<__m>

n=25, None =168, adivinhas = 
breadth_first_tree_search   < 166/ 167/ 166/<__m>
depth_first_tree_search     < 166/ 167/ 166/<__m>
greedy_search               < 166/ 167/ 166/<__m>
astar_search                < 166/ 167/ 166/<__m>

n=31, None =180, adivinhas = 
breadth_first_tree_search   < 180/ 181/ 180/<__m>
depth_first_tree_search     < 180/ 181/ 180/<__m>
greedy_search               < 180/ 181/ 180/<__m>
astar_search                < 180/ 181/ 180/<__m>


Completude (acha sempre solução):
- bfs: sim
- dfs: sim, profundidade não é infinita nem tem ciclos/estados repetidos
- greedy: sim neste caso em particular (em cada iteração apenas uma posição é atualizada, e sempre pela mesma ordem)
- a*: sim (nos n sao infinitos, em cada iteração apenas uma posição é atualizada, e sempre pela mesma ordem)

Complexidade temporal (nr nós gerados):
- bfs: O(b^d)
- dfs: O(b^m)
- greedy: O(b^m) (?)
- a*: exponencial (?)

Complexidade espacial (nr max nós em memória):
- bfs: O(b^d)
- dfs: O(b*m), espaço linear, só um caminho; Nós deixam de ser guardados em memória quando todos os seus sucessores são gerados
- greedy: O(b^m)
- a*: exponencial (?), O(b*d) (?)

Otimalidade (solução menor custo?):
m=d
- bfs: sim, custo é 1 por ação
- dfs: não (?)/ sim(?) neste caso em particular, como não há repetição de estados, só se preenche uma "casa" de cada vez/uma vez cada casa, e se tem que preencher obrigatoriamente todas as casas
- greedy: não (?) / sim (?) neste caso em particular, como não há repetição de estados, só se preenche uma "casa" de cada vez, e se tem que preencher obrigatoriamente todas as casas
- a*: sim, h(n) = 1 <= h*(n) = 1 logo heuristica e admissivel e procura e otima e equivale a greedy

- b: fator ramificação = 2
- d: profundidade solução menor custo = nr None/'2'
- m: max profundidade do espaço de estados = nr None/'2'