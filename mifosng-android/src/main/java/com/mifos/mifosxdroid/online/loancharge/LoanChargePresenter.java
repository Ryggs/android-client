package com.mifos.mifosxdroid.online.loancharge;

import com.mifos.api.DataManager;
import com.mifos.mifosxdroid.base.BasePresenter;
import com.mifos.objects.client.Charges;
import com.mifos.objects.client.Page;

import java.util.List;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Rajan Maurya on 07/06/16.
 */
public class LoanChargePresenter extends BasePresenter<LoanChargeMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public LoanChargePresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoanChargeMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadLoanChargesList(int loanId) {
        checkViewAttached();
        getMvpView().showProgressbar(true);
        if (mSubscription != null) mSubscription.unsubscribe();
        mSubscription = mDataManager.getListOfLoanCharges(loanId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Charges>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showProgressbar(false);
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            getMvpView().showFetchingError("Failed to load Charges");
                        }
                    }

                    @Override
                    public void onNext(List<Charges> chargesPage) {
                        getMvpView().showProgressbar(false);
                        getMvpView().showLoanChargesList(chargesPage);
                    }
                });
    }


}
