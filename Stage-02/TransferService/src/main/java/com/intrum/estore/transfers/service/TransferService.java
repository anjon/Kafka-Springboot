package com.intrum.estore.transfers.service;

import com.intrum.estore.transfers.model.TransferRestModel;

public interface TransferService {
    public boolean transfer(TransferRestModel productPaymentRestModel);
}
