package com.example.android.criminalintent.database;

/**
 * Created by ravensp on 7/29/2017.
 * Local DB in yer phone
 */

public class CrimeDbSchema {
    public static final class CrimeTable {
     //name of table
        public static final String Name = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
