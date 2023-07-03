package com.example.Mock.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.naming.ldap.HasControls;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.example.Mock.DAO.DraftDataObject;
import com.example.Mock.DAO.DraftedTeamsDataObject;
import com.example.Mock.StartingClasses.PlayerModel;


@Service
@Scope(value="prototype")
public class DraftServices {

    private TreeMap<Integer,DraftedTeamsDataObject> allPastsDraftsTeamsObject;
    private TreeMap<Integer,DraftDataObject> allPastsDraftsDataObject;
    private DraftedTeamsDataObject draftedTeamsDataObject;
    private DraftDataObject draftDataObject;
    private int nextDraftID = 1;

    public DraftServices() {
        
        this.allPastsDraftsTeamsObject = new TreeMap<Integer,DraftedTeamsDataObject>();
        this.allPastsDraftsDataObject = new TreeMap<Integer,DraftDataObject>();
    }

    
   public TreeMap<String,ArrayList<PlayerModel>> getTeamObject(int teamNumber) {
        return this.draftedTeamsDataObject.getTeamObject(teamNumber);
    }

    public String getTeamString(int teamNumber){
        return this.draftedTeamsDataObject.getTeamString(teamNumber);
    }

    public List<PlayerModel> getPlayersLeft() {
        return this.draftDataObject.getPlayersLeft();
    }

    public List<PlayerModel> getDraftedPlayers() {
        return this.draftDataObject.getDraftedPlayers();
    }

    public int getCurrRound() {
        return this.draftDataObject.getCurrRound();
    }

    public int getCurrPick(){
        return this.draftDataObject.getCurrPick();
    }

    public int getNextUserPick(){
        return this.draftDataObject.getNextUserPick();
    }
    
    public List<PlayerModel> getPlayersDraftedRanked() {
        return this.draftedTeamsDataObject.getPlayersDraftedRanked();
    }

    public List<PlayerModel> startDraft(String teamName, int draftSize, int desiredDraftPosition, DraftDataObject draftDataObject, DraftedTeamsDataObject draftedTeamsDataObject) {
        this.draftDataObject = draftDataObject;
        this.draftedTeamsDataObject = draftedTeamsDataObject;
        return this.draftDataObject.startDraft(teamName, draftSize, desiredDraftPosition,this.nextDraftID);
    }

    public List<PlayerModel> simTo(){
        List<PlayerModel> players = this.draftDataObject.simComputerPicks();
        if(this.draftDataObject.isDraftOver()) {
            saveDraftHistory();
        }
        return players; 
    }

    public List<PlayerModel> userDraftPick(int pick){
        List<PlayerModel> players = this.draftDataObject.userDraftPick(pick);
        if(this.draftDataObject.isDraftOver()) {
            saveDraftHistory();
        }
        return players;
    }

    public boolean isDraftOver(){
        return this.draftDataObject.isDraftOver();
    }

    public boolean checkForPastDrafts() {
        return !(this.allPastsDraftsDataObject.isEmpty() && this.allPastsDraftsTeamsObject.isEmpty());
    }

    public boolean deleteThisDraft() {
        this.draftedTeamsDataObject = null;
        this.draftDataObject = null;
        return checkForDraft();    
    }

    public boolean checkForDraft() {
        return (this.draftedTeamsDataObject != null || this.draftDataObject != null);
    }

    public HashMap<String,String> returnDraftMetaData(int nextDraftID) {
         return this.allPastsDraftsDataObject.get(nextDraftID).getDraftMetaData();
    }

    public List<PlayerModel> returnDraftedPlayers(int nextDraftID) {
        return this.allPastsDraftsDataObject.get(nextDraftID).getDraftedPlayers();
    }

    public ArrayList<HashMap<String,String>> getDraftHistoryMetaData() {
        System.out.println(this.allPastsDraftsDataObject.size());
        System.out.println(this.allPastsDraftsTeamsObject.size());
        ArrayList<HashMap<String,String>> allMetaData = new ArrayList<HashMap<String,String>>();
        for(int currDraftIndex : this.allPastsDraftsDataObject.keySet()) {
            System.out.println((currDraftIndex));
            System.out.println(this.allPastsDraftsDataObject.get(currDraftIndex).getDraftMetaData());
            allMetaData.add(this.allPastsDraftsDataObject.get(currDraftIndex).getDraftMetaData());
        }
        return allMetaData;
    }

    private void saveDraftHistory() {
        this.allPastsDraftsDataObject.put(this.nextDraftID, this.draftDataObject);
        this.allPastsDraftsTeamsObject.put(this.nextDraftID, this.draftedTeamsDataObject);
        this.nextDraftID++;
    }
}