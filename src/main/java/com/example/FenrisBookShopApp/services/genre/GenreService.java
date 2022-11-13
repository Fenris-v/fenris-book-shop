package com.example.FenrisBookShopApp.services.genre;

import com.example.FenrisBookShopApp.dto.GenreDto;
import com.example.FenrisBookShopApp.entities.genre.GenreEntity;
import com.example.FenrisBookShopApp.repositories.genre.GenreRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreService {
    private int depth = 0;

    private final GenreRepository genreRepository;
    private Map<Integer, TreeSet<GenreDto>> depthMap;
    private List<GenreDto> genres;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public GenreEntity getGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }

    public TreeSet<GenreDto> getGenres() {
        genres = genreRepository.findAllWithBookCount();
        setDepthMap();
        addChildrenToGenresDto();
        markPreLastLevel();

        return depthMap.get(0);
    }

    private void setDepthMap() {
        depthMap = new HashMap<>();
        List<GenreDto> removeList = new ArrayList<>();
        setTopLevel(removeList);
        setSubLevels(removeList);
    }

    private void setTopLevel(List<GenreDto> removeList) {
        depthMap.put(depth, new TreeSet<>());
        for (GenreDto genre : genres) {
            if (genre.getParentId() == null) {
                depthMap.get(depth).add(genre);
                removeList.add(genre);
            }
        }

        removeAddedFromList(removeList);
        depth++;
    }

    private void removeAddedFromList(List<GenreDto> removeList) {
        genres.removeAll(removeList);
        removeList.clear();
    }

    private void setSubLevels(List<GenreDto> removeList) {
        while (!genres.isEmpty() && depth < 10) {
            depthMap.put(depth, new TreeSet<>());
            for (GenreDto genre : genres) {
                addGenresToSubLevels(removeList, genre);
            }

            removeAddedFromList(removeList);
            depth++;
        }
    }

    private void addGenresToSubLevels(List<GenreDto> removeList, GenreDto genre) {
        for (GenreDto parent : depthMap.get(depth - 1)) {
            if (!parent.getId().equals(genre.getParentId())) {
                continue;
            }

            genre.setParent(parent);
            parent.setBooksCount(genre.getBooksCount());
            depthMap.get(depth).add(genre);
            removeList.add(genre);
            break;
        }
    }

    private void addChildrenToGenresDto() {
        depth = 0;
        while (depthMap.containsKey(depth + 1)) {
            addChildrenToLevel();

            depth++;
        }
    }

    private void addChildrenToLevel() {
        depthMap.get(depth).forEach(genre -> depthMap.get(depth + 1).forEach(child -> {
            if (genre.getId().equals(child.getParentId())) {
                genre.getChildren().add(child);
            }
        }));
    }

    private void markPreLastLevel() {
        depthMap.get(0).forEach(this::checkDtoAboutChildren);
    }

    private void checkDtoAboutChildren(@NotNull GenreDto genre) {
        if (!genre.getChildren().isEmpty()) {
            genre.setHasMoreThatOneChildLevel(true);
            genre.getChildren().forEach(this::checkDtoAboutChildren);
        }
    }
}
