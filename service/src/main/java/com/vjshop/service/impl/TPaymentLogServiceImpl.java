package com.vjshop.service.impl;

import com.vjshop.dao.TPaymentLogDao;
import com.vjshop.dao.TSnDao;
import com.vjshop.entity.*;
import com.vjshop.generated.db.tables.records.TPaymentLogRecord;
import com.vjshop.service.TMemberService;
import com.vjshop.service.TOrderService;
import com.vjshop.service.TPaymentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.util.List;
import static com.vjshop.entity.TPaymentLog.Type.payment;
import static com.vjshop.entity.TPaymentLog.Type.recharge;

/**
 * Service - 支付记录
 *
 * @author VJSHOP Team
 * @version 4.0
 */
@Service
public class TPaymentLogServiceImpl extends TBaseServiceImpl<TPaymentLogRecord,TPaymentLog, Long> implements TPaymentLogService {

	@Autowired
	private TPaymentLogDao tPaymentLogDao;
	@Autowired
	private TSnDao tSnDao;
	@Autowired
	private TMemberService tMemberService;
	@Autowired
	private TOrderService tOrderService;


	@Transactional(readOnly = true)
	public TPaymentLog findBySn(String sn) {
		return this.tPaymentLogDao.findBySn(sn);
	}

	@Transactional
	public void handle(TPaymentLog paymentLog) {
		Assert.notNull(paymentLog);
		Assert.notNull(paymentLog.getType());

		if (TPaymentLog.Status.wait.ordinal() != paymentLog.getStatus()) {
			return;
		}

		paymentLog = tPaymentLogDao.findWithLock(paymentLog.getId());

		if (recharge.ordinal() == paymentLog.getType()) {
			TMember member = paymentLog.getMemberObj();
			if (member != null) {
				tMemberService.addBalance(member.getId(), paymentLog.getEffectiveAmount(), TDepositLog.Type.recharge, null, null);
			}
		} else if(payment.ordinal() == paymentLog.getType()) {
			TOrder order = paymentLog.getOrderObj();
			if (order != null) {
				TPayment payment = new TPayment();
				payment.setMethod(TPayment.Method.online.ordinal());
				payment.setPaymentMethod(paymentLog.getPaymentPluginName());
				payment.setFee(paymentLog.getFee());
				payment.setAmount(paymentLog.getAmount());
				payment.setOrder(order);
				tOrderService.payment(order, payment, null);
			}
		}
		paymentLog.setStatus(TPaymentLog.Status.success.ordinal());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		paymentLog.setModifyDate(now);
		tPaymentLogDao.update(paymentLog);
	}

	@Override
	@Transactional
	public TPaymentLog save(TPaymentLog paymentLog) {
		Assert.notNull(paymentLog);

		paymentLog.setSn(tSnDao.generate(TSn.Type.paymentLog));
		return super.save(paymentLog);
	}


	public List<TPaymentLog> findByOrderId(Long... orderIds){
		List<TPaymentLog> tPaymentLogs = this.tPaymentLogDao.fetchByOrders(orderIds);
		for(TPaymentLog tPaymentLog : tPaymentLogs){
			tPaymentLog.setMemberObj(this.tMemberService.findDetails(tPaymentLog.getMember()));
			tPaymentLog.setOrderObj(this.tOrderService.find(tPaymentLog.getOrders()));
		}
		return tPaymentLogs;
	}

}