# -*- coding: utf-8 -*-
"""
Grupo al047
Student id #92485
Student id #92569
"""
#Joana Raposo
#Vicente Lorenzo

import numpy as np
import math
import random

#Calcula as probabilidades de um atributo ser positivo e negativo 
#respetivamente numa dada coluna de uma dada matriz de exemplos
def getAttributeProbability(examples, attribute_index):
    favorable_counter = 0
    against_counter = 0
    total_counter = len(examples)
    for i in range(total_counter):
        if attribute_index == None and examples[i] == 1:
            favorable_counter += 1
        elif attribute_index != None and examples[i][attribute_index] == 1:
            favorable_counter += 1
        else:
            against_counter += 1
    if total_counter == 0:
        return (0, 0)
    return (favorable_counter / total_counter, against_counter / total_counter)

#Calcula as probabilidades da classificacao de um dado atributo ter attribute_value
#ou nao (positive and negative) numa dada coluna de uma dada matriz de exemplos
def getClassificationProbability(examples, attribute_index, classi, attribute_value):
    favorable_counter = 0
    against_counter = 0
    total_counter = 0
    for i in range(len(examples)):
        if examples[i][attribute_index] == attribute_value:
            if classi[i] == 1:
                favorable_counter += 1
            else:
                against_counter += 1
            total_counter += 1
    if total_counter == 0:
        return (0, 0)
    return (favorable_counter / total_counter, against_counter / total_counter)

#Cacula a entropia de uma lista (positivo, negativo) usando a formula: I(X) = - SUM[P(x)log2(P(x))] , x ∈ X
def getEntropy(X): 
    ret = 0
    for probability in X:
        try:
            ret += (-probability*math.log(probability,2))
        except ValueError: # Quando a probabilidade e' 0
            return 0
    return ret   

#Calcula o ganho de informacao
def getInformationGain(initial_entropy, entropy):
    return initial_entropy - entropy

#Calcula o atributo ou (lista de atributos em caso de empate) de uma matriz de exemplos com o maior ganho de informacao
def getBiggestInformationGain(examples, attributes, classi, initial_entropy):
    information_gain_max = -1
    attribute_index_max = []
    #Calcular ganho de informacao para todos os atributos
    for attribute_index in attributes:
        # Calcular as probabilidades totais e condicionadas
        attribute_prob = getAttributeProbability(examples, attribute_index) #attribute_prob[0]  = probabilidade de ser 1, prob[1] = probabilidade de ser 0 
        classi_prob_0 = getClassificationProbability(examples, attribute_index, classi, 0)
        classi_prob_1 = getClassificationProbability(examples, attribute_index, classi, 1)
        # Calcular a entropia e o ganho de informacao do atributo
        attribute_entropy = attribute_prob[0] * getEntropy(classi_prob_1) + attribute_prob[1] * getEntropy(classi_prob_0)
        information_gain_aux = getInformationGain(initial_entropy, attribute_entropy)
        # Verificar se e' um ganho de informacao maximo ate agora
        if (information_gain_max == information_gain_aux):
            attribute_index_max.append(attribute_index)
        elif (information_gain_aux > information_gain_max):
            information_gain_max = information_gain_aux
            attribute_index_max = [attribute_index]
    return attribute_index_max


#Devolve o valor mais comum num conjunto de exemplos
def findPluralityValue(examples, classi, noise):
    counter_0 = 0
    counter_1 = 0
    for i in range(len(examples)): 
        if classi[i] == 0:
            counter_0 += 1
        else:
            counter_1 += 1 
    if noise and counter_0 == counter_1:
        return random.randint(0,1)
    elif counter_0 >= counter_1: 
        return 0
    else:
        return 1

#Verifica se todas as classificacoes sao iguais
def checkAllClassification(classi):
    return all(i == classi[0] for i in classi)

def getDecisionTreeLearning(examples, attributes, parent_examples, classi, parent_classi, noise, root_flag = True, force_attribute = None):
    #if examples is empty then return PLURALITY-VALUE(parent examples)
    if not examples:
        plurality = findPluralityValue(parent_examples, parent_classi, noise)
        if root_flag:
            return [0, plurality, plurality]
        return plurality
        
    #else if all examples have the same classification then return the classification
    elif(checkAllClassification(classi)):
        classification = classi[0]
        if root_flag:
            return [0, classification, classification]
        return classification
    
    #else if attributes is empty then return PLURALITY-VALUE(examples)
    elif not attributes:
        plurality = findPluralityValue(examples, classi, noise)
        if root_flag:
            return [0, plurality, plurality]
        return plurality
        
    else:
        #Calcular a entropia inicial 
        initial_entropy = getEntropy(getAttributeProbability(classi, None))
        #Calcular o atributo com maior ganho de informacao
        if not force_attribute:
            attribute_index_lst = getBiggestInformationGain(examples, attributes, classi,initial_entropy)
        else:
            attribute_index_lst = force_attribute
        #formar uma arvore com esse atributo como raiz
        res_tree = None
        res_tree_size = -1
        for attribute_index in attribute_index_lst:
            aux_tree = [attribute_index]
            for vk in [0, 1]: # Para todos os valores do atributo escolhido... (0 ou 1)
                son_attributes = attributes[:]
                son_classi = []
                son_examples = [] 
                for i in range(len(examples)):
                    if examples[i][attribute_index] == vk: #escolher as linhas cuja coluna do atributo escolhido e igual ao valor a ser avaliado
                        son_examples.append(examples[i]) 
                        son_classi.append(classi[i])
                son_attributes.remove(attribute_index)
                aux_sub_tree = getDecisionTreeLearning(son_examples, son_attributes, examples, son_classi, classi, noise, False) 
                aux_tree.append(aux_sub_tree) # respeita regras do enunciado, pq primeiro ira adicionar o caso negativo, e so depois o positivo. (ordem no vetor values e 0,1)
            aux_tree_size = len(str(aux_tree))
            if res_tree_size == -1 or (res_tree_size != -1 and aux_tree_size < res_tree_size):
                res_tree_size = aux_tree_size
                res_tree = aux_tree
        return res_tree    

#D= linhas = exemplos
#  colunas = atributos de cada feature
# Y = classificacoes
def createdecisiontree(D,Y, noise = False):
    D = (1*np.array(D)).tolist()
    Y = (1*np.array(Y)).tolist()
    attributes = [i for i in range(len(D[0]))]
    res_tree = getDecisionTreeLearning(D, attributes, [], Y , Y, noise, True, None)
    try:
        lson = res_tree[1][0]
    except TypeError:
        lson = res_tree[1]
    try:
        rson = res_tree[2][0]
    except TypeError:
        rson = res_tree[2]
    if (lson == rson):
        res_tree = getDecisionTreeLearning(D, attributes, [], Y, Y, noise, True, [lson])
    return res_tree

def classify (tree, trainingData):
    val = tree[trainingData[tree[0]]+1]
    #If its a leaf
    if not isinstance(val, list):
        return val
    else:
        return classify(val, trainingData)

if __name__ == "__main__":
    #D = [[0, 0, 0], [0, 0, 1], [0, 1, 0], [0, 1, 1], [1, 0, 0], [1, 0, 1], [1, 1, 0], [1, 1, 1]]
    #Y = [0, 1, 1, 0, 0, 1, 1, 0]
    np.random.seed(13102020)
    D = np.random.rand(5000,12)>0.5
    Y = ((D[:,1] == 0) & (D[:,6] == 0)) | ((D[:,3] == 1) & (D[:,4] == 1) | ((D[:,11] == 1) & (D[:,6] == 1)))
    T = createdecisiontree(D, Y)
    Yd = classify(T, D[3])
    print("Data: {}\nCorrect: {}\nTree: {}\nPrediction: {}".format(D, Y, T, Yd))
