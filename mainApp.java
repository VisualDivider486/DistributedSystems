class mainApp{
    public static void main(String args[]){
        Master master = new Master(Integer.parseInt(args[0]));
        System.out.println("The number of workers is " + master.getNumOfWorkers());    
    }
}    