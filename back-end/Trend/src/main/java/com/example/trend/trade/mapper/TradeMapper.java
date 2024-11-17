package com.example.trend.trade.mapper;

import com.example.trend.trade.dto.TradeReservationRegistRequestDto;
import com.example.trend.trade.dto.TradeReservationRequestDto;
import com.example.trend.trade.dto.TradeReservationResponseDto;
import com.example.trend.trade.dto.TradeReservationUpdateRequestDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("""
            UPDATE item_trade
            SET trade_price = #{tradePrice},
                trade_deposit = #{tradeDeposit},
                rental_start_date = #{tradeRentalStartDate},
                rental_end_date = #{tradeRentalEndDate}
            WHERE trade_id = #{tradeId}
            """)
    int updateReservation(TradeReservationUpdateRequestDto tradeReservationUpdateRequestDto);
}
