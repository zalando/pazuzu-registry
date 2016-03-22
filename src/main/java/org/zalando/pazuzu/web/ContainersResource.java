package org.zalando.pazuzu.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.zalando.pazuzu.service.ContainerService;
import org.zalando.pazuzu.service.ServiceException;
import org.zalando.pazuzu.web.dto.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/containers")
public class ContainersResource {

    private final ContainerService containerService;

    @Autowired
    public ContainersResource(ContainerService containerService) {
        this.containerService = containerService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ContainerDto> listContainers(@RequestParam(required = false, defaultValue = "") String name) {
        return containerService.listContainers(name, ContainerDto::ofShort);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ContainerFullDto createContainer(@RequestBody ContainerToCreateDto value) throws ServiceException {
        return containerService.createContainer(value.getName(), value.getFeatures(), ContainerFullDto::buildFull);
    }

    @RequestMapping(value = "/{containerName}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ContainerFullDto updateContainer(@PathVariable String containerName, @RequestBody ContainerToCreateDto value) throws ServiceException {
        return containerService.updateContainer(containerName, value.getName(), value.getFeatures(), ContainerFullDto::buildFull);
    }

    @RequestMapping(value = "/{containerName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ContainerFullDto getContainer(@PathVariable String containerName) throws ServiceException {
        return containerService.getContainer(containerName, ContainerFullDto::buildFull);
    }

    @RequestMapping(value = "/{containerName}", method = RequestMethod.DELETE)
    public void deleteContainer(@PathVariable String containerName) {
        containerService.deleteContainer(containerName);
    }

    @RequestMapping(value = "/{containerName}/features", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FeatureDto> getContainerFeatures(@PathVariable String containerName) throws ServiceException {
        return containerService.getContainer(containerName, ContainerFullDto::buildFull).getFeatures();
    }

    @RequestMapping(value = "/{containerName}/features", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ContainerFullDto addFeatureToContainer(@PathVariable String containerName, @RequestBody FeatureToAddDto feature) throws ServiceException {
        return containerService.addFeature(containerName, feature.getName(), ContainerFullDto::buildFull);
    }

    @RequestMapping(value = "/{containerName}/features/{featureName}", method = RequestMethod.DELETE)
    public void deleteContainerFeature(@PathVariable String containerName, @PathVariable String featureName) throws ServiceException {
        containerService.deleteFeature(containerName, featureName);
    }

    @RequestMapping(value = "/{containerName}/dockerfile", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getDockerFile(@PathVariable String containerName) throws ServiceException {
        return containerService.generateDockerFile(containerName);
    }
}