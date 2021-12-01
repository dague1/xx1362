
def dna(): 
    return "^(A|C|G|T)+$" 


def sorted():       
    return "^(9*8*7*6*5*4*3*2*1*0*)$"  


def hidden1(x):    
    
     return "^(\d|\D)*" + x + "(\d|\D)*$" 

def hidden2(x):     
    
    return "^.*" + ".*".join(x) + ".*$" 

def equation():
     return "^[\+-]?[\d]+([-\+\*/][\d]+)*(=[\+-]?[\d]+([-\+\*/][\d]+)*)?$" 

def parentheses():
    return "^(\((\((\((\((\(\))*\))*\))*\))*\))+$"

def sorted3():
    return "^[\d]*(01[23456789]|[01]2[3456789]|[012]3[456789]|[0123]4[56789]|[01234]5[6789]|[012345]6[789]|[0123456]7[89]|[01234567]89)[\d]*$"




#
from sys import stdin
import re

def main():
    def hidden1_test(): return hidden1('progp')
    def hidden2_test(): return hidden2('progp')
    tasks = [dna, sorted, hidden1_test, hidden2_test, equation, parentheses, sorted3]
    print('Skriv in teststrängar:')
    while True:
        line = stdin.readline().rstrip('\r\n')
        if line == '': break
        for task in tasks:
            result = '' if re.search(task(), line) else 'INTE '
            print('%s(): "%s" matchar %suttrycket "%s"' % (task.__name__, line, result, task()))


if __name__ == '__main__': main()