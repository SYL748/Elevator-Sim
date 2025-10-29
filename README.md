# Elevator Simulation
### Assumptions
The simulation is 25 ticks, you may exit early by doing 
```
control + c
```
run simulation in main.java
#### Elevator have 4 status <br>
GoingUp - carrying only people that tries to going up <br>
GoingDown - carrying only people that tries to going down  <br>
Idle - elevator have no request  <br>
Emergency Stop - elevator stopped completely and won't take any requests  <br>
#### Elevator Logic <br>
If the elevator is traveling to fulfill a request in one direction, it will not accept passengers traveling in the opposite direction until the current directional request is completed <br>
For example, if elevator is at floor 1 and someone pressed down button at floor 10, it won't accept any passenger that try to go up until finishing the floor 10 request <br>
The elevator will not accept any new requests for floors it has already passed <br>
For example, if the elevator is on floor 3 and moving upward, and someone on floor 1 presses the "Up" button, the elevator will not turn back to pick them up until it changes direction later <br>
#### Test Cases <br>
##### TEST 1 - Simple consecutive upward requests <br>
elevator takes all request without having anyone waiting <br>
##### TEST 2 - Simple consecutive downward <br>
elevator takes all request without having anyone waiting <br>
##### TEST 3 - Upward request received after passing the pickup floor <br>
if request send after elevator passes the pickup floor, passenger will have to wait in a backup Queue <br>
##### TEST 4 - Downward request received after passing the pickup floor <br>
if request send after elevator passes the pickup floor, passenger will have to wait in a backup Queue <br>
##### TEST 5 - Receive downward request while going up <br>
Drop off all passenger going up before getting the passengers going down <br>
##### TEST 6 - Same pickup floor but different dropoffs <br>
Perform which ever request sent first
##### TEST 7 - Emergency stop <br>
Stop all actions, and stop taking in any future requests <br>
##### TEST 8 -Complex Mixed-Direction Request Stack, Multiple opposite request stack together while elevator going up or down <br>

### Unimplemented features
No implementation of multiple elevators <br>
No implementation of weight limits in elevators <br>
No implementation of floor limit each elevator can have <br>
No asynchronous concurrency <br>
No power failure or maintenance recovery <br>
