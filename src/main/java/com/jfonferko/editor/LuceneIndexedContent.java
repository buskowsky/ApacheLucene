package com.jfonferko.editor;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "indexedContent")
@RequestScoped
public class LuceneIndexedContent {

    private List<String> indexedContent = new ArrayList<String>();
    private Analyzer analyzer = new StandardAnalyzer();
    protected static List<String> unindexedContentList = new ArrayList<String>();
    private Directory index;

    static {
        unindexedContentList.add("Spain Poland Italy");
        unindexedContentList.add("Spain Italy Germany");
        unindexedContentList.add("Germany France");
        unindexedContentList.add("France Sweden France");
        unindexedContentList.add("Spain Italy Spain");
        unindexedContentList.add("Sweden Spain");
        unindexedContentList.add("Sweden Norway France");
    }

    @PostConstruct
    public void init() {
        index = new RAMDirectory();
        analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try {
            IndexWriter w = new IndexWriter(index, config);
            for (int x = 0; x < unindexedContentList.size(); x++) {
                addDoc(w, unindexedContentList.get(x));
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIndexedContent(unindexedContentList);
    }

    private static void addDoc(IndexWriter w, String title) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        w.addDocument(doc);
    }

    public List<String> getIndexedContent() {
        return indexedContent;
    }

    public void setIndexedContent(List<String> indexedContent) {
        this.indexedContent = indexedContent;
    }

    public static List<String> getUnindexedContentList() {
        return unindexedContentList;
    }

    public static void setUnindexedContentList(List<String> unindexedContentList) {
        LuceneIndexedContent.unindexedContentList = unindexedContentList;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public Directory getIndex() {
        return index;
    }

    public void setIndex(Directory index) {
        this.index = index;
    }
}
