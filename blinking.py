#imports
from machine import Pin
from utime import sleep
led_onboard = Pin("LED", Pin.OUT)
#main loophelp
while True:
    led_onboard.toggle()
    sleep(5)