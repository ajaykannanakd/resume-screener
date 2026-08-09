package com.screener.resumescreener.service;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScoringService {

    public double computeSimilarity(String resumeText, String jobDescText) throws IOException {
        Directory index = new ByteBuffersDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(index, config)) {
            Document doc = new Document();
            doc.add(new TextField("content", resumeText, Field.Store.YES));
            writer.addDocument(doc);
        }

        try (DirectoryReader reader = DirectoryReader.open(index)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new BM25Similarity());

            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(QueryParser.escape(jobDescText));

            TopDocs results = searcher.search(query, 1);
            if (results.scoreDocs.length > 0) {
                return results.scoreDocs[0].score;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }
}