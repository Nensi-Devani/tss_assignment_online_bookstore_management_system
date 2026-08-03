package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Publisher;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.PublisherMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.PublisherRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    @Override
    public PublisherResponseDto create(PublisherRequestDto requestDto) {
        if (publisherRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Publisher already exists.");
        }

        Publisher publisher = publisherMapper.toEntity(requestDto);

        return publisherMapper.toResponseDto(publisherRepository.save(publisher));
    }

    @Override
    public PublisherResponseDto getById(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));

        return publisherMapper.toResponseDto(publisher);
    }

    @Override
    public Page<PublisherResponseDto> getAll(Pageable pageable) {
        return publisherRepository.findAll(pageable)
                .map(publisherMapper::toResponseDto);
    }

    @Override
    public PublisherResponseDto update(Long publisherId, PublisherRequestDto requestDto) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));

        if (!publisher.getName().equalsIgnoreCase(requestDto.getName()) && publisherRepository.existsByEmail(requestDto.getName())) {
            throw new DuplicateResourceException("Publisher already exists.");
        }

        publisherMapper.updateEntity(requestDto, publisher);

        return publisherMapper.toResponseDto(publisherRepository.save(publisher));
    }

    @Override
    public void delete(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));

        publisher.setStatus(Status.DELETED);

        publisherRepository.save(publisher);
    }
}
