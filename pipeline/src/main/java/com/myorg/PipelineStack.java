package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.s3.Bucket;

import software.amazon.awscdk.services.codebuild.*;
import software.amazon.awscdk.services.codecommit.*;
import software.amazon.awscdk.services.codepipeline.*;
import software.amazon.awscdk.services.codepipeline.actions.*;
import java.util.*;
import static software.amazon.awscdk.services.codebuild.LinuxBuildImage.AMAZON_LINUX_2;


public class PipelineStack extends Stack {
    public PipelineStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public PipelineStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Bucket artifactsBucket = new Bucket(this, "ArtifactsBucket");
        
        
        IRepository codeRepo = Repository.fromRepositoryName(this, "AppRepository", "sam-app");

        Pipeline pipeline = new Pipeline(this, "Pipeline", PipelineProps.builder()
                .artifactBucket(artifactsBucket).build());
        
        Artifact sourceOutput = new Artifact("sourceOutput");
        
        CodeCommitSourceAction codeCommitSource = new CodeCommitSourceAction(CodeCommitSourceActionProps.builder()
                .actionName("CodeCommit_Source")
                .repository(codeRepo)
                .output(sourceOutput)
                .build());
        
        pipeline.addStage(StageOptions.builder()
                .stageName("Source")
                .actions(Collections.singletonList(codeCommitSource))
                .build());

    }
}

