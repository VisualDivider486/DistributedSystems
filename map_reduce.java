array[2]=GPXParser("arxeio1");
all_waypoints_list=array[1];

double int n_workers;
double total_waypoints= all_wayoints_list.size();

double chunks_per_worker=Math.ceil(total_waypoints/n_workers);

int users=System.in();
static metablthth apo8hkeushs

class Waypoint{
	String time
	int elevation
	int lat
	int longt
	
	Waypoint(String time,int elevation,int lat,int longt){
	
		this.time=time;
		this.elevation=elevation;
		this.lat=lat;
		this.longt=longt;
	}
}
		
ArrayList<Waypoint> all_waypoints_list;

ArrayList<Waypoint>worker_chunkes;


//{way1 , 2 , 3, 4 , 5 ,... , n}

//arxeio 1 

//maps to workers the chunks

private void map(String user,ArrayList<Waypoints> all_waypoints_list){

	for( Waypoint waypoint_chunk : all_waipoints_list){ //estw 5
		
		worker_chunks.add(waypoint_chunk)
	
		if(worker_chunks.size()==chunks_per_worker)
			//getInputStream()
			//flush()
			//thread.start(); ??
		
			worker_chunks.clear();
		}
	}
	if(worker_chunks.size!=0){
			//getInputStream()
			//flush()
			//thread.start();
		
			worker_chunks.clear();
	}
//reduces waypoints

private void reduce(String user,ArrayList<double> statistics_partial){


	if(thread.not_in()){
		statistics_total[user]=statistics_partial
//κρισιμη περιοχη για καθε user


//prints for each user their stats

private recall_statistics(String user){
	System.out.println("the statistics for user :"+user+" are :"+ statistics_total[user] );
}

