a = [1,0,3,4,5]
b = [1,2,3,0,5]
c = [0,2,3,4,5]
e = [1,2,3,4,5]
f = [1,2,3,4,5]
d = [a,b,c,e,f]

for index1 in range(5):
    for index2 in range(index1):
        if d[index1] == d[index2]:
            print('True')