package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Publisher;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.PublisherMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.PublisherRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.PublisherService;
import com.bookstore.tss_assignment_online_bookstore_management_system.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private static final Logger logger = LoggerFactory.getLogger(PublisherServiceImpl.class);

    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    @Override
    public PublisherResponseDto create(PublisherRequestDto requestDto) {
        logger.info("Creating publisher. Name={}, Email={}", requestDto.getName(), requestDto.getEmail());

        if (publisherRepository.existsByEmail(requestDto.getEmail())) {
            logger.warn("Publisher already exists. Email={}", requestDto.getEmail());
            throw new DuplicateResourceException("Publisher already exists.");
        }

        Publisher publisher = publisherMapper.toEntity(requestDto);

        Publisher savedPublisher = publisherRepository.save(publisher);

        logger.info("Publisher created successfully. PublisherId={}", savedPublisher.getPublisherId());

        return publisherMapper.toResponseDto(savedPublisher);
    }

    @Override
    public PublisherResponseDto getById(Long publisherId) {
        logger.info("Fetching publisher. PublisherId={}", publisherId);

        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> {
                    logger.warn("Publisher not found. PublisherId={}", publisherId);
                    return new ResourceNotFoundException("Publisher not found.");
                });

        logger.info("Publisher fetched successfully. PublisherId={}", publisherId);

        return publisherMapper.toResponseDto(publisher);
    }

    @Override
    public PageResponseDto<PublisherResponseDto> getAll(Pageable pageable) {
        logger.info(
                "Fetching all publishers. Page={}, Size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<PublisherResponseDto> publishers = publisherRepository.findAll(pageable)
                .map(publisherMapper::toResponseDto);

        logger.info("Retrieved {} publishers.", publishers.getNumberOfElements());

        return PageUtil.toPageResponse(publishers);
    }

    @Override
    public PublisherResponseDto update(Long publisherId, PublisherRequestDto requestDto) {
        logger.info("Updating publisher. PublisherId={}", publisherId);

        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> {
                    logger.warn("Publisher not found. PublisherId={}", publisherId);
                    return new ResourceNotFoundException("Publisher not found.");
                });

        if (!publisher.getName().equalsIgnoreCase(requestDto.getName()) && publisherRepository.existsByEmail(requestDto.getName())) {
            logger.warn("Publisher already exists. Email={}", requestDto.getEmail());
            throw new DuplicateResourceException("Publisher already exists.");
        }

        publisherMapper.updateEntity(requestDto, publisher);

        Publisher updatedPublisher = publisherRepository.save(publisher);

        logger.info("Publisher updated successfully. PublisherId={}", updatedPublisher.getPublisherId());

        return publisherMapper.toResponseDto(updatedPublisher);
    }

    @Override
    public void delete(Long publisherId) {
        logger.info("Deleting publisher. PublisherId={}", publisherId);

        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> {
                    logger.warn("Publisher not found. PublisherId={}", publisherId);
                    return new ResourceNotFoundException("Publisher not found.");
                });

        publisher.setStatus(Status.DELETED);

        publisherRepository.save(publisher);

        logger.info("Publisher marked as DELETED. PublisherId={}", publisherId);
    }
}
