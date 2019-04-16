package com.reading.website.biz.logic;

import com.reading.website.api.base.BaseResult;
import com.reading.website.api.domain.BookDO;
import com.reading.website.api.domain.ChapterDO;
import com.reading.website.api.service.BookService;
import com.reading.website.api.service.ChapterService;
import com.reading.website.api.vo.BookInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 章节逻辑类
 *
 * @xyang010 2019/4/13
 */
@Component
@Slf4j
public class ChapterLogic {
    private final ChapterService chapterService;

    private final BookService bookService;


    @Autowired
    public ChapterLogic(ChapterService chapterService, BookService bookService) {
        this.chapterService = chapterService;
        this.bookService = bookService;
    }

    /**
     * 批量新增章节信息
     * @param chapterDOList
     * @return
     */
    public boolean batchInsertChapter(List<ChapterDO> chapterDOList) {
        Integer bookId = chapterDOList.get(0).getBookId();
        BaseResult<BookInfoVO> bookRes = bookService.selectByBookId(bookId);
        if (!bookRes.getSuccess() || bookRes.getData() == null) {
            log.warn("查询图书信息失败, bookId {}, bookRes {}", bookId, bookRes);
            return false;
        }

        BaseResult<Integer> chapterRes = chapterService.batchInsert(chapterDOList);
        if (!chapterRes.getSuccess()) {
            log.warn("批量保存章节信息失败 chapterRes {}", chapterRes);
            return false;
        }

        BookDO bookDO = new BookDO();
        bookDO.setId(bookId);
        bookDO.setChapNum(bookRes.getData().getChapNum() + chapterRes.getData());
        BaseResult<Integer> updateBookRes = bookService.insertOrUpdateBook(bookDO);
        if (!updateBookRes.getSuccess()) {
            log.warn("更新图书章节数量失败, bookId {}, beforeChapNum {}, afterChapNum {}", bookId, bookRes.getData().getChapNum(), bookDO.getChapNum());
        }
        return true;
    }
}