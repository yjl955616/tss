package com.baizhi;

import com.baizhi.entity.Poetry;
import com.baizhi.service.PoetryService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TssApplicationTests {

    @Autowired
    private PoetryService poetryService;
    @Test
    public void contextLoads() throws IOException {
        //1.指定数据的存储位置
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("E:\\index\\05"));
        //2.创建索引写入器
        //创建标准的分析对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, new IndexWriterConfig(new IKAnalyzer()));
        List<Poetry> poetries = poetryService.queryAll();
        Document document = null;
        for (Poetry poetry : poetries) {
            document = new Document();
            document.add(new IntField("id", poetry.getId(), Field.Store.YES));
            document.add(new StringField("author", poetry.getPoet().getName(), Field.Store.YES));
            document.add(new TextField("title", poetry.getTitle(), Field.Store.YES));
            document.add(new TextField("content",poetry.getContent(), Field.Store.YES));
            indexWriter.addDocument(document);
        }
        indexWriter.commit();
        indexWriter.close();
    }

}

