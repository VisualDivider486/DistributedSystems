#Walk IT

This was a project done as part of Distributed Systems' course in the 6th semester of college.
Its purpose is to take routes in xml waypoints ran from a person and returns back
statics of his current as well as total stats and tells him how he has done in comparison to the 
other's users data.

It involves a Master and as many Workers (default is two) and Clients we need.A client sends over a route
to the master ,the master distributes the payload by round robin to the workers.By the time each instance
of worker finishes it's returned back to the master,where the result is being reduced and sent to the client.
The Client has both a backend and frontend graphical interface done with Android Studio.
