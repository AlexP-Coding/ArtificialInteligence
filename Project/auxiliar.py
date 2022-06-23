COM COUNT:
32* O(32) *2 = 32^2 *2 = O(n^2)

SEM COUNT:
X1 X2 X3 X4 X5 
X1 X2 X3 X4 X5
X1 X2 X3 X4 X5
X1 X2 X3 X4 X5
X1 X2 X3 X4 X5

l1 - l2,3,4,5  	L1 dif 2,3,4,5;  2,3,4,5 dif l1 mas podem ser iguais a si proprios
l2 - l3,4,5  		l2 ja se sabe q e diff l1 , falta resto
l3 - l4, 5
l4 - l5 

= n-1 + n-2 + n-3 + n-4
= 4n 
= O(n^2 - n)


atual:
l1 - l1, l2
