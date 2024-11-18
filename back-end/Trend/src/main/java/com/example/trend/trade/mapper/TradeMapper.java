package com.example.trend.trade.mapper;

import com.example.trend.trade.dto.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Select("""
            SELECT
                i.item_id,
                i.item_name,
                i.item_price,
                i.country,
                i.province,
                i.district,
                i.town,
                i.thumbnail,

                t.rental_start_date AS trade_rental_start_date,
                t.rental_end_date AS trade_rental_end_date,
                t.trade_price,
                t.trade_deposit,
                t.payment_account_number,
                t.trade_state,
                t.payment_status,

                u1.user_id AS lessor_id,
                u2.user_id AS lessee_id,
                u1.user_nickname AS lessor_nickname,
                u2.user_nickname AS lessee_nickname
      
            FROM item_trade t
            JOIN item i ON t.item_id = i.item_id
            JOIN user u1 ON t.lessor_id = u1.user_id
            JOIN user u2 ON t.lessee_id = u2.user_id
            WHERE t.trade_id = #{tradeId}
            """)
    TradeDetailResponseDto selectTradeDetail(int tradeId);

    @Select("""
            SELECT condition_img
            FROM item_condition_image
            WHERE trade_id = #{tradeId}
            """)
    List<String> selectItemConditionImages(int tradeId);
}
