/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Font; 					import javax.microedition.lcdui.Graphics; 					import javax.microedition.lcdui.Image;  					import de.enough.polish.ui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;


/**
 * @author SAOPAYNE+KENNEDY+OLAIDE
 */
public class Wordifi extends MIDlet implements CommandListener {
    
    RecordStore names, numbers, both;
    int score,time,multiplier,acc1,acc2,acc,high1,high2,high3,high4,lowest/**/;
    String letterOf, check, questionString, question,knA, wS,highn1,highn2,highn3,highn4,highn5,newHigh/*,*/;
    Form gameForm,aboutForm,welcomeForm,instructionForm,highForm,/**/highScoreForm;
    List scoreList,menuList,levelList;  //change score to form
    Command exit,skip,back,cont,yes,no,mainMenu,playAgain;
    Alert quitGameAlert,exitAppAlert,doneAlert;
    TextField answer,highField;
    Timer timer,w;
    TimerTask tick,end,ww;
    Display display = Display.getDisplay(this);
    
    
    String words[];
    
    String level3[] = {"immunosuppression","sclerodermatus","icositetrahedron","galactopoiesis",
        "aequeosalinocalca","hepaticocholecystos","zaporizhzhya","hemoflagellate",
        "floccinaucinihili","baddelegite","dieffenbachia","sphyrnidae","vireonidae","supercafragil","absorbefacient"
            + "antidisestablishment","manageableness","gastrocnemius","psychodidae","antipericaprofied","iontophoresis","hyperacusia","womanishness"
            + "aphthous-ulcer","aphrodisiacal","encyclopaedia"};
    
    String level2[] = {"intoxication","haemorrhage","Communication","Congratulations","Interdenomination","holigol"
            + "istic","Immuno-deficiency","apocalypse","carbohydrate","hypertension","magnificient","conglomeration"
            + "" ,"intermittent","lionhearted","Scintillating","immaculate","extraordinary","justification","unintentional","simplification"
            + "","conglomerate","concatenation","bloomberg","administrator","preverification","nitty-gritty","amyotrophic"
     +"negociate","demagogue","denouncement","matriarchal","microscopic","'spirogyra","mayonnaise","millenarianism","differential","miscellaneous","paediatrician"};
    
    String level1[] = {"Eerie","Gratify","Theme","Battery","Initiate","Soccer","Inculcate","wrapper","jubilate","Erasure","Wysiwyg","Graduate",
                    "egocentric","rigorous","managerial","implicit","erudite","vindicate","message","Fashion","Stupify","impossible","prepare","successful"
            + "neccesity","initiation","application","falsify","prejudice","endorse","psychometrics","vendetta","fragmented","duplicate","expression","semantic","louvre","obfuscate","urbanise"};
    
    String levelx[] = {"sesquipedalisianism", "scriptuo-continua", "antiskepticism","leucocytozoans","sweater-dresses","atfer-cata"
            + "tesseradecades","newtownmountkennedy","Muckanaghederduahaulia","parastratiosphecomyia-stratio",""
            + "sphecomyioidied","sexmilliaquingentsexagintillion","electroencephalogram","bioluminescence","aphyllanthaceae"
            + "lordlieutenant","manichaeanism","paranoid-schizophrenia","schizophragma-hydrangea","supercalifragillisticexpialidocious"};
    
    RecordStore rs;
    
    public Wordifi(){
        skip = new Command("Skip", Command.OK, 0);
        back = new Command("Back", Command.OK, 0);
        exit = new Command("Exit", Command.OK, 0);
        cont = new Command("Continue", Command.OK, 0);
        yes = new Command("YES", Command.OK, 0);
        no = new Command("NO", Command.OK, 0);
        mainMenu = new Command("Go to Main Menu?", Command.OK, 0);
        playAgain = new Command("Play Again?", Command.OK, 0);
    }

    public void startApp() {   
        startRecords();
        refreshSettings();
        display.setCurrent(getWelcomeForm());
        w = new Timer();
        ww = new TimerTask() {

            public void run() {
                display.setCurrent(getMenuList());
            }
        };
        w.schedule(ww, 3000);
    }
    
    public void pauseApp() {
        display.setCurrent(getQuitGameAlert());
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public Alert getExitAppAlert(){
        timer.cancel();
        if (exitAppAlert==null){
            //#style mailAlert
        exitAppAlert = new Alert("Exit", "Quitting WORDIFI?!!!",
                null, AlertType.INFO, de.enough.polish.ui.StyleSheet.mailalertStyle );
        exitAppAlert.addCommand(yes);
        exitAppAlert.addCommand(no);
        exitAppAlert.setCommandListener(this);
        }
        return exitAppAlert;
    }
    
    public Alert getQuitGameAlert(){
        timer.cancel();
        if (quitGameAlert==null){
            //#style mailAlert
        quitGameAlert = new Alert("Quit Game?", "Do you want to leave game?",
                null, AlertType.INFO, de.enough.polish.ui.StyleSheet.mailalertStyle );
        quitGameAlert.addCommand(yes);
        quitGameAlert.addCommand(no);
        quitGameAlert.setCommandListener(this);
        }
        gameForm.set(3, new StringItem(null, ""));
        return quitGameAlert;
    }
    
    public Alert getDoneAlert(){
            //#style mailAlert
                doneAlert =  new Alert("Done", "Level set to "+knA+".\nClick 'OK' to continue", null, AlertType.CONFIRMATION, de.enough.polish.ui.StyleSheet.mailalertStyle );
        return doneAlert;
    }
    
    public void createTimer(){
        timer = new Timer();
        tick = new TimerTask() {

            public void run() {
                time--;
                if (answer.size()==1){
                getAnswer();
                score += score();
                nextForm();                    
                }
                display.setCurrent(getGameForm());
            }
        };
        end = new TimerTask() {

            public void run() {
                if (score > lowest){
                    display.setCurrent(getHighForm());
                } else {
                    display.setCurrent(getScoreSheet());
                }
            }
        };
    }
    
    public void startTimer(Timer t){
        t.schedule(tick, 1000, 1000);
        t.schedule(end, time*1000);
    }
    
    public Form getWelcomeForm(){
        if (welcomeForm==null){
        //#style welcome1
            welcomeForm = new Form("WORDIFI", de.enough.polish.ui.StyleSheet.welcome1Style );
        //#style welcome1
            welcomeForm.append("Welcome", de.enough.polish.ui.StyleSheet.welcome1Style );
        //#style welcome2
            welcomeForm.append("to", de.enough.polish.ui.StyleSheet.welcome2Style );
        //#style welcome3
            welcomeForm.append("WORDIFI", de.enough.polish.ui.StyleSheet.welcome3Style );
        //#style welcome4
            welcomeForm.append("The Classic Word Game", de.enough.polish.ui.StyleSheet.welcome4Style );
            
           welcomeForm.setCommandListener(this);
        }
        return welcomeForm;
    }
    
    public void makeWord(){
        Random wordRandom = new Random();
        question = words[wordRandom.nextInt(words.length-1)];
        int letterOfIndex = wordRandom.nextInt(question.length()-1);
        if (letterOfIndex<=1){
            letterOfIndex = 1;
        }
        letterOf = question.substring(letterOfIndex-1,letterOfIndex);
        if (letterOf.compareTo("-")==0){
            letterOfIndex++;
            letterOf = question.substring(letterOfIndex-1,letterOfIndex);
            letterOf = letterOf.toUpperCase();
        }
        questionString = "How many\n" + letterOf.toUpperCase() + "'s" 
                + "\nare in\n" + question.toUpperCase() + "\n";
    }
    
    public List getMenuList(){
        if (menuList==null){
        //#style listBackground
            menuList = new List("WORDIFI", List.IMPLICIT, de.enough.polish.ui.StyleSheet.listbackgroundStyle );
            //#style lists
            menuList.append("Play Game", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            menuList.append("Level", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            menuList.append("HighScore", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            menuList.append("Instruction", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            menuList.append("About", null, de.enough.polish.ui.StyleSheet.listsStyle );
            menuList.addCommand(exit);
            menuList.setCommandListener(this);
            w.cancel();
        }
        multiplier = 1;
        score = 0;
        time = 60;
        acc1 = 0;
        acc2 = 0;
        return menuList;
    }
    
    public Form getHighScoreForm(){
            //#style ltk
        highScoreForm = new Form("High Score", de.enough.polish.ui.StyleSheet.ltkStyle );
        highScoreForm.addCommand(back);
        highScoreForm.setCommandListener(this);
        try {
            rs = RecordStore.openRecordStore("rs", true);
            //#style ltk
            highScoreForm.append(new String(rs.getRecord(1)), de.enough.polish.ui.StyleSheet.ltkStyle );
            //#style ltk
            highScoreForm.append(new String(rs.getRecord(2)), de.enough.polish.ui.StyleSheet.ltkStyle );
            //#style ltk
            highScoreForm.append(new String(rs.getRecord(3)), de.enough.polish.ui.StyleSheet.ltkStyle );
            //#style ltk
            highScoreForm.append(new String(rs.getRecord(4)), de.enough.polish.ui.StyleSheet.ltkStyle );
            //#style ltk
            highScoreForm.append(new String(rs.getRecord(5)), de.enough.polish.ui.StyleSheet.ltkStyle );
        } catch (RecordStoreException ex){
            highScoreForm.append("pls refresh game");
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException ex) {
            }
        }
        return highScoreForm;
    }
    
    public Form getHighForm(){
        highField = new TextField("name", null, 10, TextField.ANY);
        if (highForm==null){
            highForm = new Form("New HighScore");
            highForm.append("Enter your name:");
            highForm.append("");
            highForm.addCommand(cont);

            highForm.setCommandListener(this);
        }
        highForm.set(1, highField);
        timer.cancel();
        return highForm;
    }
    
    public void makeNewHigh(){
        try {
            rs = RecordStore.openRecordStore("rs", true);
            highn1 = new String(rs.getRecord(1));
            high1 = Integer.valueOf(highn1.substring(16).trim()).intValue();
            highn2 = new String(rs.getRecord(2));
            high2 = Integer.valueOf(highn2.substring(16).trim()).intValue();
            highn3 = new String(rs.getRecord(3));
            high3 = Integer.valueOf(highn3.substring(16).trim()).intValue();
            highn4 = new String(rs.getRecord(4));
            high4 = Integer.valueOf(highn4.substring(16).trim()).intValue();
            newHigh = highField.getString() +Integer.toString(score);
            newHigh = highField.getString() + getSpaces(newHigh.length()) + Integer.toString(score);
            if (score > high1){
                rs.setRecord(1, newHigh.getBytes(), 0, highn1.getBytes().length);
                finishOff(1);
            } else if (score > high2){
                rs.setRecord(2, newHigh.getBytes(), 0, highn1.getBytes().length);
                finishOff(2);
            } else if (score > high3){
                rs.setRecord(3, newHigh.getBytes(), 0, highn1.getBytes().length);
                finishOff(3);
            } else if (score > high4){
                rs.setRecord(4, newHigh.getBytes(), 0, highn1.getBytes().length);
                finishOff(4);
            }
            display.setCurrent(new Alert("Saving Complete", "New Highscore Saved", null, AlertType.ALARM));
        } catch (RecordStoreException ex){
            refreshSettings();
            display.setCurrent(new Alert("Saving Error", "Parse Reunion Failed", null, AlertType.ALARM));
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public List getLevelList(){
        if (levelList==null){
        //#style listBackground
            levelList = new List("Choose Level", List.IMPLICIT, de.enough.polish.ui.StyleSheet.listbackgroundStyle );
            //#style lists
            levelList.append("Beginner", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            levelList.append("Professional", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            levelList.append("Expert", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            levelList.append("Extreme", null, de.enough.polish.ui.StyleSheet.listsStyle );
            levelList.addCommand(back);
            levelList.setCommandListener(this);
        }
        return levelList;
    }
    
    public Form getGameForm(){
        answer = new TextField("Type Answer Here   ", null, 1, TextField.NUMERIC);
        if (gameForm == null){
        gameForm = new Form("WORDIFI");
        //#style sav
        gameForm.append("Time Remaining: "+Integer.toString(time), de.enough.polish.ui.StyleSheet.savStyle );
        //#style question
        gameForm.append(questionString +"\n Score: "+ Integer.toString(score) +""
                    + "\nMultiplier: "+Integer.toString(multiplier) +/* "\n" +Integer.toString(acc2) + ""
                    + */"x\nAccuracy: " +  Integer.toString(acc) + "%", de.enough.polish.ui.StyleSheet.questionStyle );
        //#style textBox
        gameForm.append(answer, de.enough.polish.ui.StyleSheet.textboxStyle );
        gameForm.append("");
        gameForm.addCommand(skip);
        gameForm.addCommand(back);
        gameForm.setCommandListener(this);
        }
        else {
        //#style sav
            gameForm.set(0, new StringItem(null, "Time Remaining: "+Integer.toString(time)), de.enough.polish.ui.StyleSheet.savStyle );
        //#style question
            gameForm.set(1, new StringItem(null, questionString +"\n Score: "+ Integer.toString(score) +""
                    + "\nMultiplier: "+Integer.toString(multiplier) +/* "\n" +Integer.toString(acc2) + ""
                    + */"x\nAccuracy: " +  Integer.toString(acc) + "%") , de.enough.polish.ui.StyleSheet.questionStyle );
            gameForm.set(2, answer);
        }
        return gameForm;
    }
    
    public int getAnswer(){
        int check;
        if (answer.getString()==null){
            check = 0;
        }
        else if (answer.getString().equals("1")){
            check = 1;
        }
        else if ("2".equals(answer.getString())){
            check = 2;
        }
        else if (answer.getString().equals("3")){
            check = 3;
        }
        else if (answer.getString().equals("4")){
            check = 4;
        }
        else if (answer.getString().equals("5")){
            check = 5;
        }
        else if (answer.getString().equals("6")){
            check = 6;
        }
        else
            check = 0;
        return check;
    }
    
    public int mult(){
        if (getAnswer()==checkWord()){
            multiplier++;
        }
        else {
            multiplier =1;
        }
        return multiplier;
    }
    
    public int score(){
        int check = 0;
//        acc2 =+1;
        if (getAnswer()==checkWord()){
            check = mult() * getAnswer();
            acc1++;
            gameForm.set(3, new StringItem(null, "Correct"));
        }
        else {
            multiplier =1;
            gameForm.set(3, new StringItem(null, "Wrong"));
        }
        return check;
    }
    
    public Form getAboutForm(){
        if (aboutForm==null){
            //#style otherForms
            aboutForm = new Form("About WORDIFI", de.enough.polish.ui.StyleSheet.otherformsStyle );
//            //#style welcomed
            //#style otherForms
            aboutForm.append("This game was developed by some inquisitive young minds who are"
                    + " keen on educating minds with technology. It was brought to life as a result of "
                    + "hackathon with Ibeh John Kennedy,Oyewale Ademola Sao and Akande Olaide.\n"
                    + "Developed under the stable of AITI,PMCRG AND GDGOAU."
                    + "We can be reached on kbluee@yahoo.com and saopayne@gmail.com", de.enough.polish.ui.StyleSheet.otherformsStyle );
            aboutForm.addCommand(back);
            aboutForm.setCommandListener(this);
        }
        return aboutForm;
    }
    
    public Form getInstructionForm(){
        if (instructionForm==null){
            //#style otherForms
            instructionForm = new Form("Instruction", de.enough.polish.ui.StyleSheet.otherformsStyle );
            //#style otherForms
            instructionForm.append("This game is very SIMPLE.\n"
                    + "Just enter the correct number of times the letter appears in the given word.\n"
                    + "GOOD LUCK. \n \n You can also support us to win\n The TECNO APP CHALLENGE.\n"
                    + "Please download and tell your friends all about it."
                    + "Follow us on twitter @kbluue and @sao_ademola", de.enough.polish.ui.StyleSheet.otherformsStyle );
            instructionForm.addCommand(back);
            instructionForm.setCommandListener(this);
        }
        return instructionForm;
    }
    
    public List getScoreSheet(){
        if (scoreList==null){
            //#style listScore
            scoreList = new List("Score Sheet", List.IMPLICIT, de.enough.polish.ui.StyleSheet.listscoreStyle );
            //#style score
            scoreList.append("Score: ", null, de.enough.polish.ui.StyleSheet.scoreStyle );
            //#style scoret
            scoreList.append(Integer.toString(score), null, de.enough.polish.ui.StyleSheet.scoretStyle );
            //#style score
            scoreList.append("Questions Attempted: ", null, de.enough.polish.ui.StyleSheet.scoreStyle );
            //#style scoret
            scoreList.append(Integer.toString(acc2), null, de.enough.polish.ui.StyleSheet.scoretStyle );
            //#style score
            scoreList.append("Correct Answers: ", null, de.enough.polish.ui.StyleSheet.scoreStyle );
            //#style scoret
            scoreList.append(Integer.toString(acc1), null, de.enough.polish.ui.StyleSheet.scoretStyle );
            //#style score
            scoreList.append("Accuracy: ", null, de.enough.polish.ui.StyleSheet.scoreStyle );
            //#style scoret
            scoreList.append(Integer.toString(acc) + "%", null, de.enough.polish.ui.StyleSheet.scoretStyle );

            //#style lists
            scoreList.append("Play Again", null, de.enough.polish.ui.StyleSheet.listsStyle );
            //#style lists
            scoreList.append("Main Menu", null, de.enough.polish.ui.StyleSheet.listsStyle );
            scoreList.addCommand(playAgain);
            scoreList.addCommand(mainMenu);
            scoreList.setCommandListener(this);
        }
        else {
            //#style scoret
            scoreList.set(1, Integer.toString(score), null, de.enough.polish.ui.StyleSheet.scoretStyle );
            //#style scoret
            scoreList.set(3, Integer.toString(acc2), null, de.enough.polish.ui.StyleSheet.scoretStyle );
            //#style scoret
            scoreList.set(5, Integer.toString(acc1), null, de.enough.polish.ui.StyleSheet.scoretStyle );
            //#style scoret
            scoreList.set(7, Integer.toString(acc) + "%", null, de.enough.polish.ui.StyleSheet.scoretStyle );
        }
        gameForm.set(3, new StringItem(null, ""));
        timer.cancel();
        multiplier = 1;
        score = 0;
        acc1 = 0;
        acc2 = 0;
        acc = 0;
        time = 60;
        return scoreList;
    }
    
    public int checkWord(){
        question = question.toLowerCase();
        int n = 0;
        for (int i=0; i<question.length(); i++){
            check = question.substring(i, i+1);
            if (check.equals(letterOf.toLowerCase())){
                n++;
            }
        }
        return n;
    }
    
    public void nextForm(){
        if (answer.size()==1){
            makeWord();
            acc2++;
            acc = (acc1*100)/acc2;
            display.setCurrent(getGameForm());
        }        
    }
   
    public void commandAction(Command c, Displayable d) {
        if (d==welcomeForm){
            if (c==cont){
                display.setCurrent(getMenuList());
            }
        }
        if (d==menuList){
            int index = menuList.getSelectedIndex();
            switch(index){
                case 0:
                    makeWord();
                    display.setCurrent(getGameForm());//note
                    createTimer();
                    startTimer(timer);
                    break;
                case 1:
                    display.setCurrent(getLevelList());
                    break;
                case 2:
                    display.setCurrent(getHighScoreForm());
                    break;
                    
                case 3:
                    display.setCurrent(getInstructionForm());
                    break;
                case 4:
                    display.setCurrent(getAboutForm());
                    break;
            }
            if (c==exit){
                display.setCurrent(getExitAppAlert());
            }
        }
        if (d==gameForm){
            if (c==skip){
        gameForm.set(3, new StringItem(null, ""));
                makeWord();
                display.setCurrent(getGameForm());
                getAnswer();
                nextForm();
            }
            else if (c==back){
                display.setCurrent(getQuitGameAlert());
            }

        }
        if (d==levelList){
            int index = levelList.getSelectedIndex();
            switch (index){
                case 0:
                    words = level1;
                    knA ="Beginner";
                    Alert.setCurrent( display, getDoneAlert(),getMenuList() );
                    wS = "1";
                    changeSettings();
                    break;
                case 1:
                    words = level2;
                    knA ="Intermediate";
                    Alert.setCurrent( display, getDoneAlert(),getMenuList() );
                    wS = "2";
                    changeSettings();
                    break;
                case 2:
                    words = level3;
                    knA ="Expert";
                    Alert.setCurrent( display, getDoneAlert(),getMenuList() );
                    wS = "3";
                    changeSettings();
                    break;
                case 3:
                    words = levelx;
                    knA ="Extreme.\nYou better brace up. You've gotta be better, faster, smarter and have good knowled"
                            + "ge of CRAZY-LONG words";
                    Alert.setCurrent( display, getDoneAlert(),getMenuList() );
                    wS = "x";
                    changeSettings();
                    break;
            }
            if (c==back){
                    display.setCurrent(getMenuList());
            }
        }
        if (d==exitAppAlert){
            if (c==yes){
                notifyDestroyed();
            }
            if (c==no){
                display.setCurrent(getMenuList());
            }
        }
        if (d==quitGameAlert){
            if  (c==yes){
                display.setCurrent(getMenuList());
            }
            else if (c==no){
                    makeWord();
                    display.setCurrent(getGameForm());
                    createTimer();
                    startTimer(timer);
            }
        }
        
        if (d == highScoreForm){
            if (c == back){
                display.setCurrent(menuList);
            }
        
        }
        
        if (d==scoreList){
            int index = scoreList.getSelectedIndex();
            switch (index){
                case 8:
                    makeWord();
                    display.setCurrent(getGameForm());
                    createTimer();
                    startTimer(timer);
                    break;
                case 9:
                    display.setCurrent(getMenuList());
                    break;
            }
            if (c==mainMenu){
                display.setCurrent(getMenuList());
            }
            if (c==playAgain){
                    makeWord();
                    display.setCurrent(getGameForm());//note
                    createTimer();
                    startTimer(timer);//note
            }
        }
        if (d==aboutForm && c==back){
            display.setCurrent(getMenuList());
        }
        if (d==instructionForm && c==back){
            display.setCurrent(getMenuList());
        }
        if (d==highForm){
            if (c==cont){
                makeNewHigh();
                display.setCurrent(getScoreSheet());
            }
        }
    }
    
    public void startRecords(){
        try {
            rs = RecordStore.openRecordStore("rs", true);
           if (rs.getNumRecords() == 0){
               rs.addRecord("Player            00".getBytes(), 0, "Player            00".getBytes().length);
               rs.addRecord("Player             00".getBytes(), 0, "Player            00".getBytes().length);
               rs.addRecord("Player            00".getBytes(), 0, "Player            00".getBytes().length);
               rs.addRecord("Player             00".getBytes(), 0, "Player            00".getBytes().length);
               rs.addRecord("Player             00".getBytes(), 0, "Player            00".getBytes().length);
               rs.addRecord("1".getBytes(), 0, "1".getBytes().length);
           }
        } catch (RecordStoreException ex) {
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException ex) {
            }
        }
    }
    
    public void refreshSettings(){
        wS = "1";
        try {
            rs = RecordStore.openRecordStore("rs", true);
            wS = new String(rs.getRecord(6));
            String lowestH = new String(rs.getRecord(5));
            lowest = Integer.valueOf(lowestH.substring(16).trim()).intValue();
        } catch (RecordStoreException ex) {
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException ex) {
            }
        }
        if ("1".equals(wS)){
            words = level1;
        } else if ("2".equals(wS)){
            words = level2;
        } else if ("3".equals(wS)){
            words = level3;
        } else {
            words = levelx;
        }
    }
    
    public void changeSettings(){
        try {
            rs = RecordStore.openRecordStore("rs", true);
            rs.setRecord(6, wS.getBytes(), 0, wS.getBytes().length);
        } catch (RecordStoreException ex) {
            wS = "1";
            display.setCurrent(new Alert("failed", "settings change failed", null, AlertType.ALARM));
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException ex) {
            }
        }
    }
    
    public void finishOff(int start){
        try {
            rs = RecordStore.openRecordStore("rs", true);
            switch (start) {
                case 1:
                    rs.setRecord(2, highn1.getBytes(), 0, highn1.getBytes().length);
                case 2:
                    rs.setRecord(3, highn2.getBytes(), 0, highn2.getBytes().length);
                case 3:
                    rs.setRecord(4, highn3.getBytes(), 0, highn3.getBytes().length);
                case 4:
                    rs.setRecord(5, highn4.getBytes(), 0, highn4.getBytes().length);
            }
        } catch (RecordStoreException ex) {
            display.setCurrent(new Alert("Saving Error", "Parse Restore Failed", null, AlertType.ALARM));
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public String getSpaces(int lenght){
        String spaces = "";
        if (lenght == 12){
            spaces = "        ";
        } else if (lenght == 11){
            spaces = "         ";
        } else if (lenght == 10){
            spaces = "          ";
        } else if (lenght == 9){
            spaces = "           ";
        } else if (lenght == 8){
            spaces = "            ";
        } else if (lenght == 7){
            spaces = "             ";
        } else if (lenght == 6){
            spaces = "              ";
        } else if (lenght == 5){
            spaces = "               ";
        } else if (lenght == 4){
            spaces = "                ";
        } else if (lenght == 3){
            spaces = "                 ";
        } return spaces;
    }
}
