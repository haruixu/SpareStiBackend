package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeDTO;
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

    public ChallengeDTO save(ChallengeDTO challengeDTO, User user)
            throws ChallengeNotFoundException {
        Challenge newChallenge = ChallengeMapper.INSTANCE.toEntity(challengeDTO, user);
        Challenge persistedChallenge = challengeRepository.save(newChallenge);
        return ChallengeMapper.INSTANCE.toDTO(persistedChallenge);
    }

    public ChallengeDTO updateChallenge(ChallengeDTO challengeDTO, User user)
            throws ChallengeNotFoundException {
        Challenge challenge = privateGetChallenge(challengeDTO.id(), user);
        Challenge updatedChallenge = ChallengeMapper.INSTANCE.updateEntity(challenge, challengeDTO);
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
