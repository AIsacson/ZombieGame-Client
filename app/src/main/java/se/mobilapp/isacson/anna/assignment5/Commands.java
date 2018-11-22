package se.mobilapp.isacson.anna.assignment5;

import android.support.v7.app.AppCompatActivity;

abstract class Commands extends AppCompatActivity {

    public String sendRightCommand(String cmd) {
        String command;
        String [] commandSplitter = cmd.split(" ");
        if(!(isValidField(commandSplitter[1]) || isValidField(commandSplitter[2])) && commandSplitter.length > 1){
            System.err.println("Invalid input-fields.");
            return "";
        } else {
            switch(commandSplitter[0]) {
                case "REGISTER":
                    command = "REGISTER " + commandSplitter[1] + " " + commandSplitter[2];
                    break;
                case "LOGIN":
                    command = "LOGIN " + commandSplitter[1] + " " + commandSplitter[2];
                    break;
                default:
                    command = "";
                    break;
            }
        }
        return command;
    }


    private boolean isValidField(String field) {
        if(field.length() == 0) {
            System.err.println("The input-field cannot be empty.");
            return false;
        } else {
            String [] testString = field.split(" ");
            if(testString.length > 1) {
                System.err.println("The input-field must be one single line.");
                return false;
            }
        }
        return true;
    }
}
