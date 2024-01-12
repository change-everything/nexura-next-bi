package cn.nexura.nextbi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * excel处理工具类
 * @author PeiYP
 * @since 2024年01月11日 11:05
 */
@Slf4j
public class ExcelUtils {

    public static String excelToCsv(MultipartFile multipartFile) {
        // 读取数据
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误");
            throw new RuntimeException(e);
        }
        if (CollUtil.isEmpty(list)) {
            return "";
        }

        // 转换为csv
        StringBuilder stringBuilder = new StringBuilder();
        // 读取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        List<String> headerList = headerMap.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
        stringBuilder.append(StrUtil.join(",", headerList)).append("\n");
        for (int i = 1; i < list.size(); i++) {
            Map<Integer, String> integerStringMap = list.get(i);
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) integerStringMap;
            List<String> dataList = dataMap.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
            stringBuilder.append(StrUtil.join(",", dataList)).append("\n");
        }
        return stringBuilder.toString();
    }


    public static List<List<String>> excelToMap(MultipartFile multipartFile) {
        // 读取数据
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("表格处理错误");
            throw new RuntimeException(e);
        }
        if (CollUtil.isEmpty(list)) {
            return null;
        }

        // 转换为csv
        List<List<String>> dataLists = new ArrayList<>();
        // 读取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap<Integer, String>) list.get(0);
        List<String> headerList = headerMap.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
        dataLists.add(headerList);
        for (int i = 1; i < list.size(); i++) {
            Map<Integer, String> integerStringMap = list.get(i);
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) integerStringMap;
            List<String> dataList = dataMap.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
            dataLists.add(dataList);
        }
        return dataLists;
    }

}
