public class Store {

    public Store() {

    }

    public void loadScores() {
    	        /*
        Loading and saving game can be impelemented later   
                try {
                    //about record store
                    byte[] dummy = "0".getBytes();
                    rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
                    if(rsMatchTris.getNumRecords() != NoOfRecords){
                        rsMatchTris.closeRecordStore();
                        RecordStore.deleteRecordStore("MatchTris");
                        rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
                        for(int i=0; i<NoOfRecords; i++)
                            rsMatchTris.addRecord(dummy,0,dummy.length);
                        System.out.println("Added");
                    }
                    else{
                        //load i?lemlerini yap burada
                        Level = GetIntRecord(rsMatchTris,LEVEL);
                        for(int i=0; i<10; i++)
                            HighScores[i] = GetIntRecord(rsMatchTris,HIGHSCORES+i);
                        System.out.println("Level "+Level);
                        for(int i=0; i<10; i++)
                            System.out.println("HighScores "+HighScores[i]);
                        Continue = GetIntRecord(rsMatchTris,ISCONTINUE);
                    }
                    System.out.println("You have "+rsMatchTris.getNumRecords()+" records");
                    rsMatchTris.closeRecordStore();
                } catch (RecordStoreFullException e) {
                    e.printStackTrace();
                } catch (RecordStoreNotFoundException e) {
                    e.printStackTrace();
                } catch (RecordStoreException e) {
                    e.printStackTrace();
                } */
        //}
    }

    /*  
    private int GetIntRecord(RecordStore rs, int no){
        int x=0;
        try {
            byte[] temp = rs.getRecord(no);
            String temp_str = new String(temp,0,temp.length);
            x=Integer.parseInt(temp_str);
        } catch (RecordStoreNotOpenException e) {
            e.printStackTrace();
        } catch (InvalidRecordIDException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
        return x;
    }
    
    private void SetRecord(RecordStore rs,int no,int var){
        byte[] temp =( (String)(""+var) ).getBytes();
        try {
            rs.setRecord(no,temp,0,temp.length);
        } catch (RecordStoreNotOpenException e) {
            e.printStackTrace();
        } catch (InvalidRecordIDException e) {
            e.printStackTrace();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        }
    }*/

    

    

    public void SaveLoad(boolean Save) {
        long timex = System.currentTimeMillis();
        /*
        try {
            rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
            for(int i=0; i<200; i++){
                if(Save)
                    SetRecord(rsMatchTris,LOADTHEM+i,TetrisBoard[i/20][i%20]);
                else
                    TetrisBoard[i/20][i%20] = GetIntRecord(rsMatchTris,LOADTHEM+i);
            }
            if(Save) SetRecord(rsMatchTris,ISCONTINUE,1);
            for(int i=0; i<3; i++){
                if(Save)
                    SetRecord(rsMatchTris,213+i,matchstone.type[i]);
                else
                    matchstone.type[i]= GetIntRecord(rsMatchTris,213+i);
            }
            if(Save){
                SetRecord(rsMatchTris,216,matchstone.CellX);
                SetRecord(rsMatchTris,217,matchstone.CellY);
                SetRecord(rsMatchTris,218,puan);
            }
            else{
                matchstone.CellX = GetIntRecord(rsMatchTris,216);
                matchstone.CellY = GetIntRecord(rsMatchTris,217);
                puan = GetIntRecord(rsMatchTris,218);
                SetRecord(rsMatchTris,ISCONTINUE,0);
            }
            rsMatchTris.closeRecordStore();
        } catch (RecordStoreFullException e) {
            e.printStackTrace();
        } catch (RecordStoreNotFoundException e) {
            e.printStackTrace();
        } catch (RecordStoreException e) {
            e.printStackTrace();
        } */
        timex = System.currentTimeMillis() - timex;
        System.out.println("timepassed in ms " + timex + " in sec " + (timex / 1000));
    }

    public void setScore(int Level) {
        //rsMatchTris = RecordStore.openRecordStore("MatchTris",true);
        //SetRecord(rsMatchTris,LEVEL,Level);
    }
}
