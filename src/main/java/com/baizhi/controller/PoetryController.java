package com.baizhi.controller;


import com.baizhi.entity.Poet;
import com.baizhi.entity.Poetry;
import com.baizhi.service.PoetryService;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.awt.image.TileObserver;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ts")
public class PoetryController {


    @RequestMapping("/search")
    public String search(String aa, Model model, Integer nowPage) throws IOException, ParseException, InvalidTokenOffsetsException {
        if (aa != "") {

            FSDirectory fsDirectory = FSDirectory.open(Paths.get("E:\\index\\05"));
            IndexReader indexReader = DirectoryReader.open(fsDirectory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            // 查询解析器对象  作用 解析查询表达式  域名:条件
            // 参数一: 域名(默认域)
            QueryParser queryParser = new QueryParser("content", new IKAnalyzer());

            Query query = null;
            query = queryParser.parse(aa);

           // TopDocs topDocs = indexSearcher.search(query, 10);
            int pageSize = 10;
            // 分页数据
            TopDocs topDocs = null;

            //如果当前页数是为空或者0，就指定页数为第一页
            if(nowPage == null || nowPage ==0){
                nowPage = 1;
            }
            //如果说当前页数是第一页就展示查到的数据
            if(nowPage == 1 || nowPage < 1){
                topDocs = indexSearcher.search(query, pageSize);
                //如果不是第一页，就必须先获取上一页的最后一条数据
            }else if(nowPage > 1){
                topDocs = indexSearcher.search(query, (nowPage - 1) * pageSize);
                ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                ScoreDoc scoreDoc = scoreDocs[scoreDocs.length - 1];
                topDocs = indexSearcher.searchAfter(scoreDoc, query, pageSize);
            }
            //查询到的总条数
            int counts = topDocs.totalHits;
            //页面总页数
            int pageCounts = 0;

            if(counts % pageSize == 0){
                pageCounts = counts / pageSize;
            }else {
                pageCounts = counts / pageSize + 1;
            }

            // 创建高亮器对象
            Scorer scorer = new QueryScorer(query);
            // 默认高亮样式 加粗
            // 使用自定义的高亮样式
            Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:red\">", "</span>");
            Highlighter highlighter = new Highlighter(formatter, scorer);

            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            List<Poetry> list = new ArrayList<Poetry>();

            for (ScoreDoc scoreDoc : scoreDocs) {
                int docID = scoreDoc.doc;
                Document document = indexReader.document(docID);
                Poetry poetry = new Poetry();
                Poet poet = new Poet();

                //作者
                String a = highlighter.getBestFragment(new IKAnalyzer(), "author", document.get("author"));
                if (a != null) {
                    poet.setName(a);
                    poetry.setPoet(poet);
                } else {
                    poet.setName(document.get("author"));
                    poetry.setPoet(poet);
                }

                //标题
                String t = highlighter.getBestFragment(new IKAnalyzer(), "title", document.get("title"));
                if (t != null) {
                    poetry.setTitle(t);
                } else {
                    poetry.setTitle(document.get("title"));
                }
                //内容
                String c = highlighter.getBestFragment(new IKAnalyzer(), "content", document.get("content"));
                if (c != null) {
                    poetry.setContent(c);
                } else {
                    poetry.setContent(document.get("content"));
                }


                list.add(poetry);
                model.addAttribute("list", list);
                model.addAttribute("counts", counts);
                model.addAttribute("nowPage", nowPage);
                model.addAttribute("pageCounts", pageCounts);
                model.addAttribute("aa",aa);
            }
            indexReader.close();
            return "index";

        } else {
            return "redirect:/index.jsp";
        }


    }
}
