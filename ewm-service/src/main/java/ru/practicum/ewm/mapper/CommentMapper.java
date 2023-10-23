package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.dto.CommentDto;
import ru.practicum.ewm.model.dto.NewCommentDto;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "id", ignore = true)
    Comment toComment(NewCommentDto dto);

    CommentDto toCommentDto(Comment comment);
}
