"""
The main object for CatanDice
"""
import sys
from random import randint

catanFaces = {0:'gold',1:'wood',2:'wheat',3:'brick',4:'sheep',5:'ore'}

class CatanDice():
    """
    A die class from the catan dice game

    """
    _value = 0

    def __init__(self):
        """ initializes the die with a number from 0 to 5"""
        self._value = randint(0,5)

    def __repr__(self):
        return f"Catan die object: {catanFaces[self._value]}"

    def __str__(self):
        return catanFaces[self._value]

    @property
    def value(self):
        """
        A pointer to the CatanDice instance

        Returns
        -------
        TYPE
            DESCRIPTION.

        """
        return self._value

    def roll(self):
        """
        returns a random number between 0 and 5 inclusive

        Returns
        -------
        int

        """
        self._value = randint(0,5)

def main():
    """
    method to test out the object when it is run from a command line

    Returns
    -------
    None.

    """
    temp=CatanDice()
    print(temp.__repr__())
    print(catanFaces[temp.value] == temp.__str__())
    temp.roll()
    print(temp.__repr__())
    """
    should return:
    random value for dice
    a validation of the methods
    random value for dice
    """

if __name__ == "__main__":
    sys.exit(int(main() or 0))
