package tech.berjis.groupgoals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {

    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;
    private String country;
    private String country_code;

    //The edittext to input the code
    private PinView editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        editTextCode = findViewById(R.id.editTextCode);


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        String countryCode = intent.getStringExtra("countryCode");
        country = intent.getStringExtra("country");
        country_code = intent.getStringExtra("country_code");
        Toast.makeText(this, countryCode + mobile, Toast.LENGTH_SHORT).show();
        sendVerificationCode("+" + countryCode, mobile);


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = Objects.requireNonNull(editTextCode.getText()).toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }

                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });

    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String countryCode, String mobile) {
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(countryCode + mobile)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallbacks)
                        .build());
    }

    //the callback to detect the verification status
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            String UID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            String UPhone = mAuth.getCurrentUser().getPhoneNumber();

                            dbRef.child("Users").child(UID).child("country").setValue(country);
                            dbRef.child("Users").child(UID).child("country_code").setValue(country_code);
                            dbRef.child("Users").child(UID).child("user_id").setValue(country_code);
                            dbRef.child("Users").child(UID).child("user_phone").setValue(UPhone);
                            dbRef.child("Users").child(UID).child("user_image").setValue("");

                            saveCurrency(UID);

                            Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    private void saveCurrency(String UID) {
        String code;
        String name;
        String symbol;
        switch (country_code) {
            case "AX":
                code = "EUR";
                name = "Euro";
                symbol = "€";
                break;
            case "US":
                code = "USD";
                name = "United States Dollar";
                symbol = "$";
                break;
            case "GB":
                code = "GBP";
                name = "British Pound";
                symbol = "£";
                break;
            case "CZ":
                code = "CZK";
                name = "Czech Koruna";
                symbol = "Kč";
                break;
            case "TR":
                code = "TRY";
                name = "Turkish Lira";
                symbol = "₺";
                break;
            case "AE":
                code = "AED";
                name = "Emirati Dirham";
                symbol = "د.إ";
                break;
            case "AF":
                code = "AFN";
                name = "Afghanistan Afghani";
                symbol = "؋";
                break;
            case "AR":
                code = "ARS";
                name = "Argentine Peso";
                symbol = "$";
                break;
            case "AU":
                code = "AUD";
                name = "Australian Dollar";
                symbol = "$";
                break;
            case "BB":
                code = "BBD";
                name = "Barbados Dollar";
                symbol = "$";
                break;
            case "BD":
                code = "BDT";
                name = "Bangladeshi Taka";
                symbol = " Tk";
                break;
            case "BG":
                code = "BGN";
                name = "Bulgarian Lev";
                symbol = "лв";
                break;
            case "BH":
                code = "BHD";
                name = "Bahraini Dinar";
                symbol = "BD";
                break;
            case "BM":
                code = "BMD";
                name = "Bermuda Dollar";
                symbol = "$";
                break;
            case "BN":
                code = "BND";
                name = "Brunei Darussalam Dollar";
                symbol = "$";
                break;
            case "BO":
                code = "BOB";
                name = "Bolivia Bolíviano";
                symbol = "$b";
                break;
            case "BR":
                code = "BRL";
                name = "Brazil Real";
                symbol = "R$";
                break;
            case "BT":
                code = "BTN";
                name = "Bhutanese Ngultrum";
                symbol = "Nu.";
                break;
            case "BZ":
                code = "BZD";
                name = "Belize Dollar";
                symbol = "BZ$";
                break;
            case "CA":
                code = "CAD";
                name = "Canada Dollar";
                symbol = "$";
                break;
            case "CH":
                code = "CHF";
                name = "Switzerland Franc";
                symbol = "CHF";
                break;
            case "CL":
                code = "CLP";
                name = "Chile Peso";
                symbol = "$";
                break;
            case "CN":
                code = "CNY";
                name = "China Yuan Renminbi";
                symbol = "¥";
                break;
            case "CO":
                code = "COP";
                name = "Colombia Peso";
                symbol = "$";
                break;
            case "CR":
                code = "CRC";
                name = "Costa Rica Colon";
                symbol = "₡";
                break;
            case "DK":
                code = "DKK";
                name = "Denmark Krone";
                symbol = "kr";
                break;
            case "DO":
                code = "DOP";
                name = "Dominican Republic Peso";
                symbol = "RD$";
                break;
            case "EG":
                code = "EGP";
                name = "Egypt Pound";
                symbol = "£";
                break;
            case "ET":
                code = "ETB";
                name = "Ethiopian Birr";
                symbol = "Br";
                break;
            case "GE":
                code = "GEL";
                name = "Georgian Lari";
                symbol = "₾";
                break;
            case "GH":
                code = "GHS";
                name = "Ghana Cedi";
                symbol = "¢";
                break;
            case "GM":
                code = "GMD";
                name = "Gambian dalasi";
                symbol = "D";
                break;
            case "GY":
                code = "GYD";
                name = "Guyana Dollar";
                symbol = "$";
                break;
            case "HR":
                code = "HRK";
                name = "Croatia Kuna";
                symbol = "kn";
                break;
            case "HU":
                code = "HUF";
                name = "Hungary Forint";
                symbol = "Ft";
                break;
            case "ID":
                code = "IDR";
                name = "Indonesia Rupiah";
                symbol = "Rp";
                break;
            case "IL":
                code = "ILS";
                name = "Israel Shekel";
                symbol = "₪";
                break;
            case "IN":
                code = "INR";
                name = "India Rupee";
                symbol = "0";
                break;
            case "IS":
                code = "ISK";
                name = "Iceland Krona";
                symbol = "kr";
                break;
            case "JM":
                code = "JMD";
                name = "Jamaica Dollar";
                symbol = "J$";
                break;
            case "JP":
                code = "JPY";
                name = "Japanese Yen";
                symbol = "¥";
                break;
            case "KE":
                code = "KES";
                name = "Kenyan Shilling";
                symbol = "KSh";
                break;
            case "KR":
                code = "KRW";
                name = "Korea (South) Won";
                symbol = "₩";
                break;
            case "KY":
                code = "KYD";
                name = "Cayman Islands Dollar";
                symbol = "$";
                break;
            case "KZ":
                code = "KZT";
                name = "Kazakhstan Tenge";
                symbol = "лв";
                break;
            case "LA":
                code = "LAK";
                name = "Laos Kip";
                symbol = "₭";
                break;
            case "LK":
                code = "LKR";
                name = "Sri Lanka Rupee";
                symbol = "₨";
                break;
            case "LR":
                code = "LRD";
                name = "Liberia Dollar";
                symbol = "$";
                break;
            case "LT":
                code = "LTL";
                name = "Lithuanian Litas";
                symbol = "Lt";
                break;
            case "MA":
                code = "MAD";
                name = "Moroccan Dirham";
                symbol = "MAD";
                break;
            case "MD":
                code = "MDL";
                name = "Moldovan Leu";
                symbol = "MDL";
                break;
            case "MK":
                code = "MKD";
                name = "Macedonia Denar";
                symbol = "ден";
                break;
            case "MN":
                code = "MNT";
                name = "Mongolia Tughrik";
                symbol = "₮";
                break;
            case "MU":
                code = "MUR";
                name = "Mauritius Rupee";
                symbol = "₨";
                break;
            case "MW":
                code = "MWK";
                name = "Malawian Kwacha";
                symbol = "MK";
                break;
            case "MX":
                code = "MXN";
                name = "Mexico Peso";
                symbol = "$";
                break;
            case "MY":
                code = "MYR";
                name = "Malaysia Ringgit";
                symbol = "RM";
                break;
            case "MZ":
                code = "MZN";
                name = "Mozambique Metical";
                symbol = "MT";
                break;
            case "NA":
                code = "NAD";
                name = "Namibia Dollar";
                symbol = "$";
                break;
            case "NG":
                code = "NGN";
                name = "Nigeria Naira";
                symbol = "₦";
                break;
            case "NI":
                code = "NIO";
                name = "Nicaragua Cordoba";
                symbol = "C$";
                break;
            case "NO":
                code = "NOK";
                name = "Norway Krone";
                symbol = "kr";
                break;
            case "NP":
                code = "NPR";
                name = "Nepal Rupee";
                symbol = "₨";
                break;
            case "NZ":
                code = "NZD";
                name = "New Zealand Dollar";
                symbol = "$";
                break;
            case "OM":
                code = "OMR";
                name = "Oman Rial";
                symbol = "﷼";
                break;
            case "PE":
                code = "PEN";
                name = "Peru Sol";
                symbol = "S/.";
                break;
            case "PG":
                code = "PGK";
                name = "Papua New Guinean Kina";
                symbol = "K";
                break;
            case "PH":
                code = "PHP";
                name = "Philippines Peso";
                symbol = "₱";
                break;
            case "PK":
                code = "PKR";
                name = "Pakistan Rupee";
                symbol = "₨";
                break;
            case "PL":
                code = "PLN";
                name = "Poland Zloty";
                symbol = "zł";
                break;
            case "PY":
                code = "PYG";
                name = "Paraguay Guarani";
                symbol = "Gs";
                break;
            case "QA":
                code = "QAR";
                name = "Qatar Riyal";
                symbol = "﷼";
                break;
            case "RO":
                code = "RON";
                name = "Romania Leu";
                symbol = "lei";
                break;
            case "RS":
                code = "RSD";
                name = "Serbia Dinar";
                symbol = "Дин.";
                break;
            case "RU":
                code = "RUB";
                name = "Russia Ruble";
                symbol = "₽";
                break;
            case "SA":
                code = "SAR";
                name = "Saudi Arabia Riyal";
                symbol = "﷼";
                break;
            case "SE":
                code = "SEK";
                name = "Sweden Krona";
                symbol = "kr";
                break;
            case "SG":
                code = "SGD";
                name = "Singapore Dollar";
                symbol = "$";
                break;
            case "SO":
                code = "SOS";
                name = "Somalia Shilling";
                symbol = "S";
                break;
            case "SR":
                code = "SRD";
                name = "Suriname Dollar";
                symbol = "$";
                break;
            case "TH":
                code = "THB";
                name = "Thailand Baht";
                symbol = "฿";
                break;
            case "TT":
                code = "TTD";
                name = "Trinidad and Tobago Dollar";
                symbol = "TT$";
                break;
            case "TW":
                code = "TWD";
                name = "Taiwan New Dollar";
                symbol = "NT$";
                break;
            case "TZ":
                code = "TZS";
                name = "Tanzanian Shilling";
                symbol = "TSh";
                break;
            case "UA":
                code = "UAH";
                name = "Ukraine Hryvnia";
                symbol = "₴";
                break;
            case "UG":
                code = "UGX";
                name = "Ugandan Shilling";
                symbol = "USh";
                break;
            case "UY":
                code = "UYU";
                name = "Uruguay Peso";
                symbol = "$U";
                break;
            case "VE":
                code = "VEF";
                name = "Venezuela Bolívar";
                symbol = "Bs";
                break;
            case "VN":
                code = "VND";
                name = "Viet Nam Dong";
                symbol = "₫";
                break;
            case "YE":
                code = "YER";
                name = "Yemen Rial";
                symbol = "﷼";
                break;
            case "ZA":
                code = "ZAR";
                name = "South Africa Rand";
                symbol = "R";
                break;
            default:
                code = "USD";
                name = "United States Dollar";
                symbol = "$";
                break;
        }

        dbRef.child("Users").child(UID).child("currency_code").setValue(code);
        dbRef.child("Users").child(UID).child("currency_symbol").setValue(symbol);
        dbRef.child("Users").child(UID).child("currency_name").setValue(name);
    }

}
