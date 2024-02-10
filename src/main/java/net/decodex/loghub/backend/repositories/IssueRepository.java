package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Issue;
import net.decodex.loghub.backend.domain.models.LogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

public interface IssueRepository extends MongoRepository<Issue, String> {
    List<Issue> findByLogEntriesIn(@NonNull Collection<LogEntry> logEntries);
}
