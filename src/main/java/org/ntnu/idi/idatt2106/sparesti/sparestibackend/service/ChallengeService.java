package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeAlreadyCompletedException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.ChallengeMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.ChallengeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public Page<ChallengeDTO> getChallengesByUser(User user, Pageable pageable)
            throws ChallengeNotFoundException {
        return challengeRepository.findByUser(user, pageable).map(ChallengeMapper.INSTANCE::toDTO);
    }

    public Page<ChallengeDTO> getActiveChallenges(User user, Pageable pageable) {
        return challengeRepository
                .findAllByCompletedOnIsNullAndUser(user, pageable)
                .map(ChallengeMapper.INSTANCE::toDTO);
    }

    public Page<ChallengeDTO> getCompletedChallenges(User user, Pageable pageable) {
        return challengeRepository
                .findAllByCompletedOnIsNotNullAndUser(user, pageable)
                .map(ChallengeMapper.INSTANCE::toDTO);
    }

    public ChallengeDTO completeChallenge(Long challengeId, User user) {
        Challenge challenge = findChallengeByIdAndUser(challengeId, user);
        if (challenge.getCompletedOn() != null) {
            throw new ChallengeAlreadyCompletedException(challengeId);
        }
        challenge.setCompletedOn(ZonedDateTime.now());
        challengeRepository.save(challenge);
        return ChallengeMapper.INSTANCE.toDTO(challenge);
    }

    private Challenge findChallengeByIdAndUser(Long challengeId, User user) {
        return challengeRepository
                .findByIdAndUser(challengeId, user)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    public ChallengeDTO save(ChallengeCreateDTO challengeCreateDTO, User user)
            throws ChallengeNotFoundException {
        Challenge newChallenge = ChallengeMapper.INSTANCE.toEntity(challengeCreateDTO, user);
        Challenge persistedChallenge = challengeRepository.save(newChallenge);
        return ChallengeMapper.INSTANCE.toDTO(persistedChallenge);
    }

    public ChallengeDTO updateChallenge(Long id, ChallengeUpdateDTO challengeUpdateDTO, User user)
            throws ChallengeNotFoundException {
        Challenge challenge = privateGetChallenge(id, user);
        Challenge updatedChallenge =
                ChallengeMapper.INSTANCE.updateEntity(challenge, challengeUpdateDTO);
        Challenge persistedChallenge = challengeRepository.save(updatedChallenge);
        return ChallengeMapper.INSTANCE.toDTO(persistedChallenge);
    }

    public ChallengeDTO getChallenge(Long challengeId, User user)
            throws ChallengeNotFoundException {
        return ChallengeMapper.INSTANCE.toDTO(privateGetChallenge(challengeId, user));
    }

    public void deleteChallenge(Long challengeId, User user) throws ChallengeNotFoundException {
        Challenge challenge = privateGetChallenge(challengeId, user);
        challengeRepository.delete(challenge);
    }

    private Challenge privateGetChallenge(Long challengeId, User user) {

        return challengeRepository
                .findByIdAndUser(challengeId, user)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }
}
