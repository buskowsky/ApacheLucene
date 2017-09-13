package com.mkyong.editor;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "editor")
@RequestScoped
public class EditorBean {

    @ManagedProperty(value = "#{indexedContent}")
    private LuceneIndexedContent luceneIndexedContent;

    public final String TEXT_TO_SEARCH_PLACEHOLDER = "Text to search";

    private int hits;
    private String value = "This editor is provided by PrimeFaces";
    private String textToSearch;
    private List<String> luceneResult;
    private List<String> indexedContent;

    @PostConstruct
    public void init() {
        indexedContent = new ArrayList<String>();
        indexedContent = luceneIndexedContent.getIndexedContent();
    }

    public void testLucene() throws IOException, ParseException {
        Query q = new QueryParser("title", luceneIndexedContent.getAnalyzer()).parse(textToSearch);
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(luceneIndexedContent.getIndex());
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        luceneResult = new ArrayList<String>();

        this.hits = hits.length;

        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title"));
            luceneResult.add(d.get("title"));
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getLuceneResult() {
        return luceneResult;
    }

    public void setLuceneResult(List<String> luceneResult) {
        this.luceneResult = luceneResult;
    }

    public String getTextToSearch() {
        return textToSearch;
    }

    public void setTextToSearch(String textToSearch) {
        this.textToSearch = textToSearch;
    }

    public String getTEXT_TO_SEARCH_PLACEHOLDER() {
        return TEXT_TO_SEARCH_PLACEHOLDER;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public List<String> getIndexedContent() {
        return indexedContent;
    }

    public void setIndexedContent(List<String> indexedContent) {
        this.indexedContent = indexedContent;
    }

    public LuceneIndexedContent getLuceneIndexedContent() {
        return luceneIndexedContent;
    }

    public void setLuceneIndexedContent(LuceneIndexedContent luceneIndexedContent) {
        this.luceneIndexedContent = luceneIndexedContent;
    }
}