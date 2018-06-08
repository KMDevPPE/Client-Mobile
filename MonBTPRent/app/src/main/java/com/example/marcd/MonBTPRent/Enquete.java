package com.example.marcd.MonBTPRent;

import com.example.marcd.MonBTPRent.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by marcd on 12/02/2018.
 */

public class Enquete
{
    private static  HashMap<String, Client> lesUsers =  new HashMap<String, Client>(); ;


    public static void ajouterUser (Client unUser)
    {
        lesUsers.put(unUser.getEmail(),unUser);
    }
    public static boolean existe (Client unUser)
    {
        if (lesUsers.containsKey(unUser.getEmail())) return true;
        else return false;
    }
    public static void ajouterUneReponse(String nom, String question, float score)
    {
        lesUsers.get(nom).ajouterReponse(question, score);
    }

    public static float getLaMoyenne (String nom)
    {
        return lesUsers.get(nom).getMoyenne();
    }

    public static ArrayList<String> getLesResultats ()
    {
        ArrayList<String> lesRes = new ArrayList<String>();
        for (Client unuser : lesUsers.values())
        {
            lesRes.add("email : "+unuser.getEmail()+" Moyenne : "+ unuser.getMoyenne());
        }
        return lesRes;
    }
    public static ArrayList<Integer> getLesSmileys()
    {
        ArrayList<Integer> lesSmiley = new ArrayList<Integer>();
        for (Client unParticipant : lesUsers.values())
        {
            if (unParticipant.getMoyenne()<10)
            {
                lesSmiley.add(R.drawable.smileyc);
            }else if (unParticipant.getMoyenne()<14) {
                lesSmiley.add(R.drawable.smileym);
            }else{
                lesSmiley.add(R.drawable.smiley);
            }
        }
        return lesSmiley;
    }
}
