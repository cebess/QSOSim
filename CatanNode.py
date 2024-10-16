import sys

class CatanNode(object):
    """This is a node for game 1 of the Catan Dice game"""
    def __init__(self,name:str="",roadout1=None,roadout2=None,structureBuilt:bool=False,value:int=0,islands:list=[-1]):
        self._name = name
        self._road1=roadout1
        self._road2=roadout2
        self._built=structureBuilt
        self._value = value
        self._islands = islands
    
    def __repr__(self):
        return(f"CatanNode {self._name} between islands {self._islands} built:{self._built}")

    def __str__(self):
        if len(self._islands)==1:
            if self._islands == [-1]:
                return(f"{self._name} with an empty list of islands")
            else:
                return(f"{self._name} on island {self._islands[0]}")
        else:
            return(f"{self._name} between islands {self._islands[0]} and {self._islands[1]}")

    def set_built(self):
        self._built = True

def main():
    """
    method to test out the object when it is run from a command line

    Returns
    -------
    None.

    """
    temp=CatanNode("test0")
    print(temp.__repr__())
    temp=CatanNode("test1",None,None,False,0,[-1])
    print(temp.__repr__())
    temp=CatanNode("test2",None,None,False,0,[1,2])
    print(temp.__repr__())
    temp.set_built()
    print(temp.__repr__())

if __name__ == "__main__":
    sys.exit(int(main() or 0))