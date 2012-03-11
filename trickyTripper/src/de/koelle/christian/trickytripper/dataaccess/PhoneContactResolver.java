package de.koelle.christian.trickytripper.dataaccess;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import de.koelle.christian.trickytripper.model.PhoneContact;

public class PhoneContactResolver {

    private final ContentResolver mResolver;

    public PhoneContactResolver(ContentResolver cr) {
        mResolver = cr;
    }

    public ArrayList<PhoneContact> findContactByNameString(String nameSubstr) {
        String[] projection = { Phone.CONTACT_ID, StructuredName.DISPLAY_NAME };
        String selection = Data.IN_VISIBLE_GROUP + "=1 AND " +
                Phone.DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = { "%" + nameSubstr + "%" };
        if (nameSubstr == null) {
            selection = null;
            selectionArgs = null;
        }
        Cursor phoneCursor = null;
        ArrayList<PhoneContact> contacts = new ArrayList<PhoneContact>();
        try {
            phoneCursor = mResolver.query(Data.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    StructuredName.DISPLAY_NAME);
            int idCol = phoneCursor.getColumnIndex(Phone.CONTACT_ID);
            int nameCol = phoneCursor.getColumnIndex(StructuredName.DISPLAY_NAME);
            while (phoneCursor.moveToNext()) {
                long id = phoneCursor.getLong(idCol);
                String displayName = phoneCursor.getString(nameCol);
                PhoneContact contact = new PhoneContact();
                contact.displayName = displayName;
                contact.id = String.valueOf(id);
                contacts.add(contact);
            }
        }
        finally {
            if (phoneCursor != null)
                phoneCursor.close();
        }
        return contacts;
    }

    public ArrayList<PhoneContact> findContactByNameString2(String nameSubstr) {
        String[] projection = { ContactsContract.Data._ID, ContactsContract.Data.DISPLAY_NAME };
        String selection = ContactsContract.Data.IN_VISIBLE_GROUP + "=1 AND " +
                ContactsContract.Data.DISPLAY_NAME + " LIKE ?";

        String[] selectionArgs = { "%" + nameSubstr + "%" };
        if (nameSubstr == null) {
            selection = null;
            selectionArgs = null;
        }
        Cursor phoneCursor = null;
        ArrayList<PhoneContact> contacts = new ArrayList<PhoneContact>();
        try {
            phoneCursor = mResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    ContactsContract.Data.DISPLAY_NAME);
            int idCol = phoneCursor.getColumnIndex(ContactsContract.Data._ID);
            int nameCol = phoneCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME);
            while (phoneCursor.moveToNext()) {
                long id = phoneCursor.getLong(idCol);
                String displayName = phoneCursor.getString(nameCol);
                if (displayName == null || displayName.length() < 2) {
                    continue;
                }
                PhoneContact contact = new PhoneContact();
                contact.displayName = displayName;
                contact.id = String.valueOf(id);
                contacts.add(contact);
            }
        }
        finally {
            if (phoneCursor != null)
                phoneCursor.close();
        }
        return contacts;
    }
}