# Walk IT

This was a project done as part of Distributed Systems' course in the 6th semester of college.
Its purpose is to take routes in xml waypoints ran from a person and returns back
statics of his current as well as total stats and tells him how he has done in comparison to the 
other's users data.

It involves a Master and as many Workers (default is two) and Clients we need.A client sends over a route
to the master ,the master distributes the payload by round robin to the workers.By the time each instance
of worker finishes it's returned back to the master,where the result is being reduced and sent to the client.
The Client has both a backend and frontend graphical interface done with Android Studio.


![image](https://github.com/VisualDivider486/DistributedSystems/assets/72051251/28472280-2ab4-4a70-bb23-a6194b175fc4) 
![image](https://github.com/VisualDivider486/DistributedSystems/assets/72051251/2e300bd8-9d0c-4c6f-9a23-537c5c793001)
