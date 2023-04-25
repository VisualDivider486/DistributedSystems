class MasterMultithread implements Runnable{

    private int id;

    public MasterMultithread(int id){
        this.id = id;
    }

    int getId(){
        return id;
    }

    void setId(int id){
        this.id = id;
    }

    @Override
    public void run(){
        for (int i = 1; i < 5; i++){
            System.out.println(i + " from thread " + getId());
        }
    }
}