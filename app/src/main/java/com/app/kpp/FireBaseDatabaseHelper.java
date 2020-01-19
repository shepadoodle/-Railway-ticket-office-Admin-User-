package com.app.kpp;

import androidx.annotation.NonNull;

import com.app.kpp.Models.Admin;
import com.app.kpp.Models.BlackList;
import com.app.kpp.Models.Train;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseDatabaseHelper {


    private DatabaseReference mReferenceAdmins;
    private DatabaseReference mReferenceTrains;
    private DatabaseReference mReferenceBlacklist;

    private List<Admin> adminsList = new ArrayList<>();
    private List<Train> trainsList = new ArrayList<>();
    private List<BlackList> Blacklist = new ArrayList<>();

    public interface DataAdminStatus {
        void dataAdminStatus(List<Admin> adminList, List<String> keys);
    }

    public interface DataTrainStatus {
        void dataTrainStatus(List<Train> trainsList, List<String> keys);
    }

    public interface DataBlacklistStatus {
        void dataBlacklistStatus(List<BlackList> Blacklist, List<String> keys);
    }

    public FireBaseDatabaseHelper() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        
        mReferenceAdmins = mDatabase.getReference("Admins");
        mReferenceTrains = mDatabase.getReference("Trains");
        mReferenceBlacklist = mDatabase.getReference("Users Blacklist");
    }

    public void readAdmins(final DataAdminStatus dataAdminStatus) {
        mReferenceAdmins.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminsList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Admin adminTab = keyNode.getValue(Admin.class);
                    adminsList.add(adminTab);
                   /* for (int i=0; i < adminsList.size(); i++)
                    {
                        System.out.println(adminsList.get(i));
                    }*/
                }
                dataAdminStatus.dataAdminStatus(adminsList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readTrains(final DataTrainStatus dataTrainStatus) {
        mReferenceTrains.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trainsList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Train trainTab = keyNode.getValue(Train.class);
                    trainsList.add(trainTab);
                   /* for (int i=0; i < adminsList.size(); i++)
                    {
                        System.out.println(adminsList.get(i));
                    }*/
                }
                dataTrainStatus.dataTrainStatus(trainsList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readBlacklist(final DataBlacklistStatus dataBlacklistStatus) {
        mReferenceBlacklist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Blacklist.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    BlackList BlackListtab = keyNode.getValue(BlackList.class);
                    Blacklist.add(BlackListtab);
                   /* for (int i=0; i < adminsList.size(); i++)
                    {
                        System.out.println(adminsList.get(i));
                    }*/
                }
                dataBlacklistStatus.dataBlacklistStatus(Blacklist, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

