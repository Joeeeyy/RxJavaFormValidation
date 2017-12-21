package jjoey.com.rxvalidations;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by JosephJoey on 9/17/2017.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.til_FullName)
    TextInputLayout til_FullName;

    @BindView(R.id.fullNameTET)
    TextInputEditText fullNameTET;

    @BindView(R.id.til_occupation)
    TextInputLayout til_occupation;

    @BindView(R.id.occuTET)
    TextInputEditText occuTET;

    @BindView(R.id.til_college_orgn)
    TextInputLayout til_college_orgn;

    @BindView(R.id.college_orgnTET)
    TextInputEditText college_orgnTET;

    @BindView(R.id.doneBtn)
    Button doneBtn;

    @BindView(R.id.bloodGroup)
    RadioGroup bloodGroup;

    private String bGroup;
    private boolean isEnabled = false;

    private boolean a_plus, a_minus, b_plus, b_minus, o_plus, o_minus, ab_plus, ab_minus, dunno;

    private rx.Observable<CharSequence> nameObservable;
    private rx.Observable<CharSequence> occupObservable;
    private rx.Observable<CharSequence> collObservable;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        compositeSubscription = new CompositeSubscription();

        selectBloodGroup();
        validateUI();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });

    }

    private void validateUI() {

        doneBtn.setEnabled(false);

        nameObservable = RxTextView.textChanges(fullNameTET);
        Subscription nameSubscription = nameObservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence sequence) {
                        Validations.hideNameError(til_FullName);
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence sequence) {
                        return !TextUtils.isEmpty(sequence);
                    }
                })
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CharSequence sequence) {
                        boolean isValidName = Validations.validateName(sequence.toString());
                        if (!isValidName) {
                            Validations.showNameError(til_FullName);
                        } else {
                            Validations.hideNameError(til_FullName);
                        }
                    }
                });
        compositeSubscription.add(nameSubscription);

        occupObservable = RxTextView.textChanges(occuTET);
        Subscription occuSubscription = occupObservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence sequence) {
                        Validations.hideOccupationError(til_occupation);
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence sequence) {
                        return !TextUtils.isEmpty(sequence.toString());
                    }
                })
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence sequence) {
                        boolean isValidOccupation = Validations.validateOccupation(sequence.toString());
                        if (!isValidOccupation) {
                            Validations.showOccupationError(til_occupation);
                        } else {
                            Validations.hideOccupationError(til_occupation);
                        }
                    }
                });
        compositeSubscription.add(occuSubscription);

        collObservable = RxTextView.textChanges(college_orgnTET);
        Subscription collegeSubscription = collObservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence sequence) {
                        Validations.hideCollegeError(til_college_orgn);
                    }
                })
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence sequence) {
                        return !TextUtils.isEmpty(sequence.toString());
                    }
                })
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence sequence) {
                        boolean isValidCollege = Validations.validateCollege(sequence.toString());
                        if (!isValidCollege) {
                            Validations.showCollegeError(til_college_orgn);
                        } else {
                            Validations.hideCollegeError(til_college_orgn);
                        }
                    }
                });
        compositeSubscription.add(collegeSubscription);

        Subscription allFields = rx.Observable.combineLatest(nameObservable, occupObservable, collObservable, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence name, CharSequence occup, CharSequence college) {
                boolean isValidName = Validations.validateName(name.toString());
                boolean isValidOccupation = Validations.validateOccupation(occup.toString());
                boolean isValidCollege = Validations.validateCollege(college.toString());

                return isValidName && isValidOccupation && isValidCollege;
            }
        }).observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            doneBtn.setBackgroundColor(getResources().getColor(R.color.purpleColor));
                            doneBtn.setEnabled(true);
                            isEnabled = true;
                            Log.d(TAG, "Enabled State is:\t" + isEnabled);
                        } else {
                            doneBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                            doneBtn.setEnabled(false);
                            isEnabled = false;
                            Log.d(TAG, "Enabled State is:\t" + isEnabled);
                        }
                    }
                });

        compositeSubscription.add(allFields);

    }

    private void selectBloodGroup(){
        bloodGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.a_plus:
                        a_plus = true;
                        a_minus = b_plus = b_minus = ab_plus = ab_minus = o_plus = o_minus = dunno = false;
                        Log.d(TAG, "A+ Checked");
                        break;
                    case R.id.a_minus:
                        a_minus = true;
                        a_plus = b_plus = b_minus = ab_plus = ab_minus = o_plus = o_minus = dunno = false;
                        Log.d(TAG, "A- Checked");
                        break;
                    case R.id.b_plus:
                        b_plus = true;
                        a_minus = a_plus = b_minus = ab_plus = ab_minus = o_plus = o_minus = dunno = false;
                        Log.d(TAG, "B+ Checked");
                        break;
                    case R.id.b_minus:
                        b_minus = true;
                        a_plus = b_plus = a_minus = ab_plus = ab_minus = o_plus = o_minus = dunno = false;
                        Log.d(TAG, "B- Checked");
                        break;
                    case R.id.ab_plus:
                        ab_plus = true;
                        a_minus = b_plus = b_minus = a_plus = ab_minus = o_plus = o_minus = dunno = false;
                        Log.d(TAG, "AB+ Checked");
                        break;
                    case R.id.ab_minus:
                        ab_minus = true;
                        a_plus = b_plus = b_minus = ab_plus = a_minus = o_plus = o_minus = dunno = false;
                        Log.d(TAG, "AB- Checked");
                        break;
                    case R.id.o_plus:
                        o_plus = true;
                        a_minus = b_plus = b_minus = ab_plus = ab_minus = a_plus = o_minus = dunno = false;
                        Log.d(TAG, "O+ Checked");
                        break;
                    case R.id.o_minus:
                        o_minus = true;
                        a_plus = b_plus = b_minus = ab_plus = ab_minus = o_plus = a_minus = dunno = false;
                        Log.d(TAG, "O- Checked");
                        break;
                    case R.id.type_idk:
                        dunno = true;
                        a_plus = a_minus = ab_plus = ab_minus = b_plus = b_minus = o_plus = o_minus = false;
                        Log.d(TAG, "Don't Know Checked");
                }

            }
        });

        bGroup = "";

        if (a_plus) {
            bGroup = "A+";
        } else if (a_minus) {
            bGroup = "A-";
        } else if (b_plus) {
            bGroup = "B+";
        } else if (b_minus) {
            bGroup = "B-";
        } else if (ab_plus) {
            bGroup = "AB+";
        } else if (ab_minus) {
            bGroup = "AB-";
        } else if (o_plus) {
            bGroup = "O+";
        } else if (o_minus) {
            bGroup = "O-";
        } else if (dunno) {
            bGroup = "Don't Know";
        }
    }

    private void validateInputs() {

        //bloodGroup = findViewById(R.id.bloodGroup);

        if (isEnabled == true && bGroup.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.blood_grp_sel_error), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Success...Form validated", Snackbar.LENGTH_LONG).show();
            // You can start a new activity here
        }

    }

}
