#imports
import machine
import utime
led_onboard = machine.Pin("LED", machine.Pin.OUT)
#main loophelp
while True:
    led_onboard.toggle()
    utime.sleep(5)