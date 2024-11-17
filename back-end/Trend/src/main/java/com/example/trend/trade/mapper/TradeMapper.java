package com.example.trend.trade.mapper;

import com.example.trend.trade.dto.TradeReservationRegistRequestDto;
import com.example.trend.trade.dto.TradeReservationRequestDto;
import com.example.trend.trade.dto.TradeReservationResponseDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TradeMapper {
    @Select("""
            SELECT item_id, available_rental_start_date, available_rental_end_date, item_name, item_price, country, province, district, town, thumbnail,
            (SELECT user_nickname FROM user WHERE user_id = #{lessorId}) AS lessor_nickname,
            (SELECT user_nickname FROM user WHERE user_id = #{lesseeId}) AS lessee_nickname
            FROM item
            WHERE item_id = #{itemId}
            """)
    TradeReservationResponseDto selectReservationInfo(TradeReservationRequestDto tradeReservationRequestDto);

    @Insert("""
            INSERT
            INTO item_trade (item_id, lessor_id, lessee_id, trade_price, trade_deposit, payment_account_number, rental_start_date, rental_end_date)
            VALUES (#{itemId}, #{lessorId}, #{lesseeId}, #{tradePrice}, #{tradeDeposit}, #{paymentAccountNumber}, #{tradeRentalStartDate}, #{tradeRentalEndDate})
            """)
    int insertReservation(TradeReservationRegistRequestDto tradeReservationRegistRequestDto);
}
