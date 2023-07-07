package dto;

import model.HasId;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardDTO implements HasId<Long> {

    private List<LeaderboardItemDTO> entries;

    public LeaderboardDTO(List<LeaderboardItemDTO> entries) {
        this.entries = entries;
    }

    public LeaderboardDTO() {
        entries = new ArrayList<>();
    }

    private void addEntry(LeaderboardItemDTO entry) {
        entries.add(entry);
    }

    public List<LeaderboardItemDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<LeaderboardItemDTO> entries) {
        this.entries = entries;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long aLong) {

    }
}
