package com.example.springboot.service;

import com.example.springboot.vo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface IWorkspaceService {

    /**
     * Get Business Data according to the time period
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * Get Order Over View
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * Get Dish Over View
     * @return
     */
    DishOverViewVO getDishOverView();

    /**
     * Get Turnover according to the time period
     * @param beginTime
     * @param endTime
     * @return
     */
    TurnoverReportVO getTurnover(LocalDate beginTime, LocalDate endTime);

    /**
     * Get Order Statistics according to the time period
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * Get Top 10 according to the time period
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

}
